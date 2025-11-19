package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.model.event.MemorialDeletedEvent;
import windeath44.server.memorial.domain.memorial.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;

@Service
@RequiredArgsConstructor
public class MemorialDeleteService {
    private final MemorialRepository memorialRepository;
    private final MemorialPullRequestRepository memorialPullRequestRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void deleteMemorial(Long memorialId) {
        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(MemorialNotFoundException::new);

        // Get the latest approved content before deletion
        MemorialPullRequest latestApprovedPR = memorialPullRequestRepository
                .findMemorialPullRequestByMemorialAndState(memorial, MemorialPullRequestState.APPROVED);
        String content = (latestApprovedPR != null && latestApprovedPR.getToCommit() != null)
                ? latestApprovedPR.getToCommit().getContent()
                : "";

        // Publish event before deletion
        applicationEventPublisher.publishEvent(
                MemorialDeletedEvent.of(memorial, content)
        );

        // Delete memorial (cascade will handle related entities)
        memorialRepository.delete(memorial);
    }
}