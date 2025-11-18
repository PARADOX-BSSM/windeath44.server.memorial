package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestAlreadyApprovedException;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.MemorialMergePermissionDeniedException;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.memorial.model.event.MemorialMergedEvent;

@Service
@RequiredArgsConstructor
public class MemorialMergeService {
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final MemorialChiefService memorialChiefService;
  private final ApplicationEventPublisher applicationEventPublisher;
  
  @Transactional
  public void mergeMemorialCommit(String userId, MemorialMergeRequestDto memorialMergeRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialMergeRequestDto.memorialPullRequestId())
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    Memorial memorial = memorialPullRequest.getMemorial();

    // Check if user is a chief of this memorial
    if (!memorialChiefService.getChiefs(memorial.getMemorialId()).contains(userId)) {
      throw new MemorialMergePermissionDeniedException();
    }

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
    applicationEventPublisher.publishEvent(
            MemorialMergedEvent.of(memorialPullRequest)
    );
  }


}
