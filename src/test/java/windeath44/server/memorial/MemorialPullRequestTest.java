package windeath44.server.memorial;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialPullRequest;
import windeath44.server.memorial.domain.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.repository.MemorialRepository;
import windeath44.server.memorial.domain.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.exception.MemorialPullRequestAlreadySentException;
import windeath44.server.memorial.domain.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;
import windeath44.server.memorial.domain.service.MemorialPullRequestService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemorialPullRequestTest {
  @Autowired
  private MemorialCommitService memorialCommitService;
  @Autowired
  private MemorialPullRequestService memorialPullRequestService;
  @Autowired
  private MemorialRepository memorialRepository;
  @Autowired
  private MemorialPullRequestRepository memorialPullRequestRepository;

  String userId;

  @BeforeEach
  public void setUp() {
    userId = "test";
  }

  @Test
  void createMemorialPullRequestSuccess() {
    // 커밋 PR 성공
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto(1L, "test");
    memorialCommitService.createMemorialCommit(userId, memorialCommitRequestDto);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto = new MemorialPullRequestRequestDto(1L);
    memorialPullRequestService.createMemorialPullRequest(userId, memorialPullRequestRequestDto);
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(1L)
            .orElse(null);
    assertNotNull(memorialPullRequest);
  }

  @Test
  void createMemorialPullRequestFail1() {
    // 커밋 PR 실패(메모리얼 커밋 Not Found)
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto(1L, "test");
    memorialCommitService.createMemorialCommit(userId, memorialCommitRequestDto);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto = new MemorialPullRequestRequestDto( 2L);
    assertThrows(MemorialCommitNotFoundException.class, () -> memorialPullRequestService.createMemorialPullRequest(userId, memorialPullRequestRequestDto));
  }

  @Test
  void createMemorialPullRequestFail2() {
    // 커밋 PR 실패(해당 메모리얼 커밋이 이미 풀 리퀘스트 보내짐)
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto(1L, "test");
    memorialCommitService.createMemorialCommit(userId, memorialCommitRequestDto);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto1 = new MemorialPullRequestRequestDto(1L);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto2 = new MemorialPullRequestRequestDto(1L);
    memorialPullRequestService.createMemorialPullRequest(userId, memorialPullRequestRequestDto1);
    assertThrows(MemorialPullRequestAlreadySentException.class, () -> memorialPullRequestService.createMemorialPullRequest(userId, memorialPullRequestRequestDto2));
  }
}
