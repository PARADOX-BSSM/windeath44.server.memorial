package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestAlreadyApprovedException;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.CompareContentsResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialMergeableResponseDto;
import windeath44.server.memorial.global.diff_match_patch;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class MemorialMergeService {
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final diff_match_patch dmp = new diff_match_patch();

  public MemorialMergeableResponseDto validateMergeable(String userId, MemorialMergeRequestDto memorialMergeRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialMergeRequestDto.memorialPullRequestId())
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    Memorial memorial = memorialPullRequest.getMemorial();
    MemorialPullRequest latestMemorialPullRequest = memorialPullRequestRepository.findMemorialPullRequestByMemorialAndState(memorial, MemorialPullRequestState.APPROVED);
    if (latestMemorialPullRequest != null) {
      String latestContent = latestMemorialPullRequest.getMemorialCommit().getContent();
      String newContent = memorialPullRequest.getMemorialCommit().getContent();

      CompareContentsResponseDto comparison = compareContents(latestContent, newContent);
      return new MemorialMergeableResponseDto(
              memorialPullRequest.getMemorialPullRequestId(),
              latestMemorialPullRequest.getMemorialPullRequestId(),
              comparison.mergeable(),
              comparison.conflict()
      );
    }
    return new MemorialMergeableResponseDto(
            memorialPullRequest.getMemorialPullRequestId(),
            null,
            true,
            null
    );
  }

  @Transactional
  public void mergeMemorialCommit(String userId, MemorialMergeRequestDto memorialMergeRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialMergeRequestDto.memorialPullRequestId())
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    Memorial memorial = memorialPullRequest.getMemorial();

    if(memorialPullRequest.isAlreadyApproved())
      throw new MemorialPullRequestAlreadyApprovedException();

    MemorialPullRequest latestApprovedMemorialPullRequest = memorialPullRequestRepository.findMemorialPullRequestByMemorialAndState(memorial, MemorialPullRequestState.APPROVED);
    memorialPullRequest.approve();
    memorialPullRequest.merger(userId);

    if (latestApprovedMemorialPullRequest != null) {
      latestApprovedMemorialPullRequest.store();
      memorialPullRequestRepository.save(latestApprovedMemorialPullRequest);
    }

    memorialPullRequestRepository.save(memorialPullRequest);
  }

  private CompareContentsResponseDto compareContents(String text1, String text2) {
    LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(text1, text2);
    dmp.diff_cleanupSemantic(diffs);
    Boolean mergeable = true;
    StringBuilder conflict = new StringBuilder();

    for (int i = 0; i < diffs.size(); i++) {
      diff_match_patch.Diff diff = diffs.get(i);
      if (i < diffs.size() - 1) {
        diff_match_patch.Diff next = diffs.get(i + 1);
        if (diff.operation == diff_match_patch.Operation.DELETE && next.operation == diff_match_patch.Operation.INSERT) {
          mergeable = false;
          conflict.append("\n\n>>>>>>>>>>>>>>>>>>>> original\n");
          conflict.append(diff.text).append("\n");
          conflict.append(">>>>>>>>>>>>>>>>>>>> changed\n");
          conflict.append(next.text).append("\n");
          conflict.append("<<<<<<<<<<<<<<<<<<<< end\n");
          i += 1; // skip the next diff as it's already processed
          continue;
        }
      }
      conflict.append(diff.text);
    }

    return new CompareContentsResponseDto(mergeable, conflict.toString());
  }
}
