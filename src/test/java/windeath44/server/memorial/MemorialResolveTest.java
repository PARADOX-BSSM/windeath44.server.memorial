package windeath44.server.memorial;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialResolveRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialPullRequestResponseDto;
import windeath44.server.memorial.domain.memorial.service.MemorialCommitService;
import windeath44.server.memorial.domain.memorial.service.MemorialMergeService;
import windeath44.server.memorial.domain.memorial.service.MemorialPullRequestService;
import windeath44.server.memorial.domain.memorial.service.MemorialResolveService;

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

  String userId;

  @BeforeEach
  public void setUp() {
    userId = "test";
  }

  @Test
  void resolveTest1() {
    // mergeable: 충돌이 있을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto memorialCommitRequestDto1 = new MemorialCommitRequestDto( 1L, "test");
    MemorialCommitRequestDto memorialCommitRequestDto2 = new MemorialCommitRequestDto( 1L, "asdf");
    memorialCommitService.createMemorialCommit(userId, memorialCommitRequestDto1);
    memorialCommitService.createMemorialCommit(userId, memorialCommitRequestDto2);

    MemorialPullRequestRequestDto memorialPullRequestRequestDto1 = new MemorialPullRequestRequestDto( 1L);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto2 = new MemorialPullRequestRequestDto( 2L);
    memorialPullRequestService.createMemorialPullRequest(userId, memorialPullRequestRequestDto1);
    memorialPullRequestService.createMemorialPullRequest(userId, memorialPullRequestRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto( 1L);
    memorialMergeService.mergeMemorialCommit(userId, memorialMergeRequestDto1);
    MemorialResolveRequestDto memorialResolveRequestDto = new MemorialResolveRequestDto(2L, "tedf");
    memorialResolveService.resolve(userId, memorialResolveRequestDto);

    assertNotNull(memorialCommitService.findMemorialCommitById(3L));
    MemorialPullRequestResponseDto memorialPullRequestResponseDto1 = memorialPullRequestService.findMemorialPullRequestById(1L);
    MemorialPullRequestResponseDto memorialPullRequestResponseDto3 = memorialPullRequestService.findMemorialPullRequestById(3L);
    assertNotNull(memorialPullRequestResponseDto3);
    assertEquals(MemorialPullRequestState.APPROVED, memorialPullRequestResponseDto3.state());
    assertEquals(MemorialPullRequestState.STORED, memorialPullRequestResponseDto1.state());
  }
}
