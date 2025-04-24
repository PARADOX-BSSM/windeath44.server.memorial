package windeath44.server.memorial;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialMergeableResponseDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;
import windeath44.server.memorial.domain.service.MemorialMergeService;
import windeath44.server.memorial.domain.service.MemorialPullRequestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemorialMergeTest {
  @Autowired
  private MemorialCommitService memorialCommitService;
  @Autowired
  private MemorialPullRequestService memorialPullRequestService;
  @Autowired
  private MemorialMergeService memorialMergeService;

  @Autowired
  private MemorialRepository memorialRepository;
  @Autowired
  private MemorialPullRequestRepository memorialPullRequestRepository;

  @Test
  void mergeMemorialCommitCase2() {
    // 이전에 APPROVED 된 PR이 있을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto memorialCommitRequestDto1 = new MemorialCommitRequestDto("test", 1L, "test");
    MemorialCommitRequestDto memorialCommitRequestDto2 = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto1);
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto2);

    MemorialPullRequestRequestDto memorialPullRequestRequestDto1 = new MemorialPullRequestRequestDto("test1", 1L);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto2 = new MemorialPullRequestRequestDto("test2", 2L);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto1);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialMergeService.mergeMemorialCommit(memorialMergeRequestDto1);
    memorialMergeService.mergeMemorialCommit(memorialMergeRequestDto2);

    MemorialPullRequest memorialPullRequest1 = memorialPullRequestRepository.findById(1L)
            .orElse(null);
    MemorialPullRequest memorialPullRequest2 = memorialPullRequestRepository.findById(2L)
            .orElse(null);

    assertNotNull(memorialPullRequest1);
    assertNotNull(memorialPullRequest2);
    assertEquals(MemorialPullRequestState.STORED, memorialPullRequest1.getState());
    assertEquals(MemorialPullRequestState.APPROVED, memorialPullRequest2.getState());
  }

  @Test
  void mergeableTest1() {
    // mergeable: 추가만 있을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto memorialCommitRequestDto1 = new MemorialCommitRequestDto("test", 1L, "test");
    MemorialCommitRequestDto memorialCommitRequestDto2 = new MemorialCommitRequestDto("test", 1L, "testtest");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto1);
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto2);

    MemorialPullRequestRequestDto memorialPullRequestRequestDto1 = new MemorialPullRequestRequestDto("test1", 1L);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto2 = new MemorialPullRequestRequestDto("test2", 2L);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto1);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialMergeService.mergeMemorialCommit(memorialMergeRequestDto1);
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialMergeService.validateMergeable(memorialMergeRequestDto2);

    assertNotNull(memorialMergeableResponseDto);
    assertEquals(true, memorialMergeableResponseDto.mergeable());
  }

  @Test
  void mergeableTest2() {
    // mergeable: 삭제만 있을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto memorialCommitRequestDto1 = new MemorialCommitRequestDto("test", 1L, "test");
    MemorialCommitRequestDto memorialCommitRequestDto2 = new MemorialCommitRequestDto("test", 1L, "");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto1);
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto2);

    MemorialPullRequestRequestDto memorialPullRequestRequestDto1 = new MemorialPullRequestRequestDto("test1", 1L);
    MemorialPullRequestRequestDto memorialPullRequestRequestDto2 = new MemorialPullRequestRequestDto("test2", 2L);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto1);
    memorialPullRequestService.createMemorialPullRequest(memorialPullRequestRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialMergeService.mergeMemorialCommit(memorialMergeRequestDto1);
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialMergeService.validateMergeable(memorialMergeRequestDto2);

    assertNotNull(memorialMergeableResponseDto);
    assertEquals(true, memorialMergeableResponseDto.mergeable());
  }

  @Test
  void mergeableTest3() {
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
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialMergeService.mergeMemorialCommit(memorialMergeRequestDto1);
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialMergeService.validateMergeable(memorialMergeRequestDto2);

    assertNotNull(memorialMergeableResponseDto);
    assertEquals(false, memorialMergeableResponseDto.mergeable());
  }
}
