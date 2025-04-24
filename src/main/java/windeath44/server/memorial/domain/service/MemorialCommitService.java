package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.entity.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.exception.*;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;
import windeath44.server.memorial.domain.mapper.MemorialCommitMapper;
import windeath44.server.memorial.domain.presentation.dto.response.CompareContentsResponseDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialMergeableResponseDto;
import windeath44.server.memorial.global.diff_match_patch;
import windeath44.server.memorial.global.diff_match_patch.Diff;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class MemorialCommitService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
  private final MemorialRepository memorialRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final diff_match_patch dmp = new diff_match_patch();

  public void createMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto) {
    Memorial memorial = memorialRepository.findById(memorialCommitRequestDto.memorialId())
            .orElseThrow(MemorialNotFoundException::new);
    memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(memorialCommitRequestDto, memorial));
  }

  public void createMemorialPullRequest(MemorialPullRequestDto memorialPullRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialPullRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    if (memorialPullRequestExists != null) {
      throw new MemorialPullRequestAlreadySentException();
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, memorialPullRequestDto.userId());
    memorialPullRequestRepository.save(memorialPullRequest);
  }

  public MemorialMergeableResponseDto validateMergeable(MemorialMergeRequestDto memorialMergeRequestDto) {
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

  public void mergeMemorialCommit(MemorialMergeRequestDto memorialMergeRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialMergeRequestDto.memorialPullRequestId())
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    Memorial memorial = memorialPullRequest.getMemorial();

    if(memorialPullRequest.isAlreadyApproved())
      throw new MemorialPullRequestAlreadyApprovedException();

    MemorialPullRequest latestApprovedMemorialPullRequest = memorialPullRequestRepository.findMemorialPullRequestByMemorialAndState(memorial, MemorialPullRequestState.APPROVED);
    memorialPullRequest.approve();
    memorialPullRequest.merger(memorialMergeRequestDto.userId());

    if (latestApprovedMemorialPullRequest != null) {
      latestApprovedMemorialPullRequest.store();
      memorialPullRequestRepository.save(latestApprovedMemorialPullRequest);
    }

    memorialPullRequestRepository.save(memorialPullRequest);
  }

  private CompareContentsResponseDto compareContents(String text1, String text2) {
    LinkedList<Diff> diffs = dmp.diff_main(text1, text2);
    dmp.diff_cleanupSemantic(diffs);
    Boolean mergeable = true;
    StringBuilder conflict = new StringBuilder();

    for (int i = 0; i < diffs.size()-1; i++) {
      Diff diff = diffs.get(i);
      Diff next = diffs.get(i+1);
      if (diff.operation == diff_match_patch.Operation.DELETE && next.operation == diff_match_patch.Operation.INSERT) {
        mergeable = false;
        i += 1;
        conflict.append("\n\n>>>>>>>>>>>>>>>>>>>> original\n");
        conflict.append(diff.text).append("\n");
        conflict.append(">>>>>>>>>>>>>>>>>>>> changed\n");
        conflict.append(next.text).append("\n\n");
      }
      else conflict.append(diff.text);
    }
    conflict.append(diffs.getLast().text);

    return new CompareContentsResponseDto(mergeable, conflict.toString());
  }
}
