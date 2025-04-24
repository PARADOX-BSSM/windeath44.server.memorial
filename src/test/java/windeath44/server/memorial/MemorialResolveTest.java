package windeath44.server.memorial;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialResolveRequestDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialPullRequestResponseDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;
import windeath44.server.memorial.domain.service.MemorialMergeService;
import windeath44.server.memorial.domain.service.MemorialPullRequestService;
import windeath44.server.memorial.domain.service.MemorialResolveService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemorialResolveTest {
  @Autowired
  private MemorialCommitService memorialCommitService;
  @Autowired
  private MemorialPullRequestService memorialPullRequestService;
  @Autowired
  private MemorialMergeService memorialMergeService;
  @Autowired
  private MemorialResolveService memorialResolveService;

  @Autowired
  private MemorialRepository memorialRepository;

  @Test
  void resolveTest1() {
    // mergeable: 충돌이 있을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto memorialCommitRequestDto1 = new MemorialCommitRequestDto("test", 1L, "test");
    MemorialCommitRequestDto memorialCommitRequestDto2 = new MemorialCommitRequestDto("test", 1L, "asdf");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto1);
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto2);

    MemorialPullRequestRequestDto memorialPullRequestRequestDto1 = new MemorialPullRequestRequestDto("test1", 1L);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto2 = new MemorialPullRequestRequestDto("test2", 2L);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto1);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    memorialMergeService.mergeMemorialCommit(memorialMergeRequestDto1);
    MemorialResolveRequestDto memorialResolveRequestDto = new MemorialResolveRequestDto("test", 2L, "tedf");
    memorialResolveService.resolve(memorialResolveRequestDto);

    assertNotNull(memorialCommitService.findMemorialCommitById(3L));
    MemorialPullRequestResponseDto memorialPullRequestResponseDto1 = memorialPullRequestService.findMemorialPullRequestById(1L);
    MemorialPullRequestResponseDto memorialPullRequestResponseDto3 = memorialPullRequestService.findMemorialPullRequestById(3L);
    assertNotNull(memorialPullRequestResponseDto3);
    assertEquals(MemorialPullRequestState.APPROVED, memorialPullRequestResponseDto3.state());
    assertEquals(MemorialPullRequestState.STORED, memorialPullRequestResponseDto1.state());
  }
}
