package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.dto.response.CompareContentsResponseDto;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.memorial.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestAlreadySentException;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.memorial.mapper.MemorialPullRequestMapper;
import windeath44.server.memorial.global.diff_match_patch;
import java.util.LinkedList;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialPullRequestResponseDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialPullRequestDiffResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialPullRequestService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final MemorialPullRequestMapper memorialPullRequestMapper;
  private final diff_match_patch dmp = new diff_match_patch();

  @Transactional
  public MemorialPullRequestResponseDto createMemorialPullRequest(String userId, MemorialPullRequestRequestDto memorialPullRequestRequestDto) {
    MemorialCommit toCommit = memorialCommitRepository.findById(memorialPullRequestRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = toCommit.getMemorial();

    // Find current approved version (fromCommit)
    MemorialPullRequest latestApprovedPR = memorialPullRequestRepository.findMemorialPullRequestByMemorialAndState(memorial, MemorialPullRequestState.APPROVED);
    MemorialCommit fromCommit = (latestApprovedPR != null) ? latestApprovedPR.getToCommit() : null;

    // Check if PR already exists for these commits
    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByFromCommitAndToCommit(fromCommit, toCommit);
    if (memorialPullRequestExists != null) {
      MemorialPullRequest memorialPullRequest = new MemorialPullRequest(fromCommit, toCommit, memorial, userId, MemorialPullRequestState.REJECTED);
      memorialPullRequestRepository.save(memorialPullRequest);
      throw new MemorialPullRequestAlreadySentException();
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(fromCommit, toCommit, memorial, userId, MemorialPullRequestState.PENDING);
    memorialPullRequestRepository.save(memorialPullRequest);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }

  @Transactional
  public MemorialPullRequestResponseDto createMemorialPullRequestApproved(String userId, MemorialPullRequestRequestDto memorialPullRequestRequestDto) {
    MemorialCommit toCommit = memorialCommitRepository.findById(memorialPullRequestRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = toCommit.getMemorial();

    // Find current approved version (fromCommit)
    MemorialPullRequest latestApprovedPR = memorialPullRequestRepository.findMemorialPullRequestByMemorialAndState(memorial, MemorialPullRequestState.APPROVED);
    MemorialCommit fromCommit = (latestApprovedPR != null) ? latestApprovedPR.getToCommit() : null;

    // Check if PR already exists for these commits
    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByFromCommitAndToCommit(fromCommit, toCommit);
    if (memorialPullRequestExists != null) {
      MemorialPullRequest memorialPullRequest = new MemorialPullRequest(fromCommit, toCommit, memorial, userId, MemorialPullRequestState.REJECTED);
      memorialPullRequestRepository.save(memorialPullRequest);
      throw new MemorialPullRequestAlreadySentException();
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(fromCommit, toCommit, memorial, userId, MemorialPullRequestState.APPROVED);
    memorialPullRequestRepository.save(memorialPullRequest);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }

  public MemorialPullRequestResponseDto findMemorialPullRequestById(Long memorialPullRequestId) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialPullRequestId)
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }

  @Transactional
  public void deleteMemorialPullRequestsByMemorialId(Long memorialId) {
    List<MemorialPullRequest> memorialPullRequests = memorialPullRequestRepository.findMemorialPullRequestsByMemorial_MemorialId(memorialId);
    memorialPullRequestRepository.deleteAll(memorialPullRequests);
  }

  public List<MemorialPullRequestResponseDto> findMemorialPullRequestsByMemorialId(Long memorialId) {
    List<MemorialPullRequest> memorialPullRequests = memorialPullRequestRepository.findMemorialPullRequestsByMemorial_MemorialId(memorialId);
    return memorialPullRequests.stream()
            .map(memorialPullRequestMapper::toMemorialPullRequestResponseDto)
            .toList();
  }

  public MemorialPullRequestDiffResponseDto getMemorialPullRequestDiff(Long memorialPullRequestId) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialPullRequestId)
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    
    String fromContent = "";
    String toContent = "";
    
    // Get content from fromCommit (base version)
    if (memorialPullRequest.getFromCommit() != null) {
      fromContent = memorialPullRequest.getFromCommit().getContent();
    }
    
    // Get content from toCommit (new version)  
    if (memorialPullRequest.getToCommit() != null) {
      toContent = memorialPullRequest.getToCommit().getContent();
    }
    
    // Use compareContents method for better diff
    CompareContentsResponseDto comparison = compareContents(fromContent, toContent);
    
    return new MemorialPullRequestDiffResponseDto(
            memorialPullRequest.getMemorialPullRequestId(),
            comparison.conflict(), // Using the enhanced diff from compareContents
            !comparison.mergeable(), // hasConflicts is inverse of mergeable
            memorialPullRequest.getUserId(),
            memorialPullRequest.getCreatedAt()
    );
  }

  public CompareContentsResponseDto compareContents(String text1, String text2) {
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
