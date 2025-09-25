package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialRejectRequestDto;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.memorial.service.MemorialRejectService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemorialRejectServiceTest {

    @Autowired
    private MemorialRejectService memorialRejectService;

    @Autowired
    private MemorialPullRequestRepository memorialPullRequestRepository;

    @Test
    void rejectMemorialPullRequestSuccess() {
        // Setup
        MemorialPullRequest pullRequest = new MemorialPullRequest();
        pullRequest.setState(MemorialPullRequestState.PENDING);
        memorialPullRequestRepository.save(pullRequest);

        Long pullRequestId = pullRequest.getMemorialPullRequestId();
        MemorialRejectRequestDto rejectRequestDto = new MemorialRejectRequestDto("user1", pullRequestId);

        // Execute
        memorialRejectService.rejectMemorialPullRequest(rejectRequestDto);

        // Verify
        MemorialPullRequest rejectedPullRequest = memorialPullRequestRepository.findById(pullRequestId)
                .orElseThrow();
        assertEquals(MemorialPullRequestState.REJECTED, rejectedPullRequest.getState(), 
                "Pull request state should be REJECTED");
    }

    @Test
    void rejectNonExistingMemorialPullRequestFails() {
        // Setup
        Long nonExistingPullRequestId = 999L;
        MemorialRejectRequestDto rejectRequestDto = new MemorialRejectRequestDto("user1", nonExistingPullRequestId);

        // Execute & Verify
        assertThrows(MemorialPullRequestNotFoundException.class, () -> 
            memorialRejectService.rejectMemorialPullRequest(rejectRequestDto),
            "Should throw MemorialPullRequestNotFoundException for non-existing pull request"
        );
    }
}
