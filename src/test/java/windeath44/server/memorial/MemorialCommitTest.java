package windeath44.server.memorial;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;
import windeath44.server.memorial.domain.service.MemorialMergeService;
import windeath44.server.memorial.domain.service.MemorialPullRequestService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemorialCommitTest {

  @Autowired
  private MemorialCommitService memorialCommitService;
  @Autowired
  private MemorialPullRequestService memorialPullRequestService;
  @Autowired
  private MemorialMergeService memorialMergeService;

  @Autowired
  private MemorialRepository memorialRepository;
  @Autowired
  private MemorialCommitRepository memorialCommitRepository;
  @Autowired
  private MemorialPullRequestRepository memorialPullRequestRepository;

  @Test
  void createMemorialCommitSuccess() {
    // 메모리얼 커밋 성공
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto dto = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(dto);

    MemorialCommit memorialCommit = memorialCommitRepository.findById(1L)
            .orElse(null);
    assertNotNull(memorialCommit);
  }

  @Test
  void createMemorialCommitFail() {
    // 메모리얼 커밋 실패
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto dto = new MemorialCommitRequestDto("test", 2L, "test");
    assertThrows(MemorialNotFoundException.class, () -> memorialCommitService.createMemorialCommit(dto));
  }

  @Test
  void mergeMemorialCommitCase1() {
    // 이전에 APPROVE 된 PR이 없을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto = new MemorialPullRequestRequestDto("test", 1L);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto);
    MemorialMergeRequestDto memorialMergeRequestDto = new MemorialMergeRequestDto("test", 1L);
    memorialMergeService.mergeMemorialCommit(memorialMergeRequestDto);
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(1L)
            .orElse(null);
    assertNotNull(memorialPullRequest);
    assertEquals(MemorialPullRequestState.APPROVED, memorialPullRequest.getState());
  }
}
