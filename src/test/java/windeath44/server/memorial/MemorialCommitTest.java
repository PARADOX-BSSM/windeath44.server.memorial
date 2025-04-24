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
import windeath44.server.memorial.domain.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.exception.MemorialPullRequestAlreadyApprovedException;
import windeath44.server.memorial.domain.exception.MemorialPullRequestAlreadySentException;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;
import windeath44.server.memorial.domain.presentation.dto.response.MemorialMergeableResponseDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemorialCommitTest {

  @Autowired
  private MemorialCommitService memorialCommitService;

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
  void createMemorialPullRequestSuccess() {
    // 커밋 PR 성공
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto);
    MemorialPullRequestDto memorialPullRequestDto = new MemorialPullRequestDto("test", 1L);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto);
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(1L)
            .orElse(null);
    assertNotNull(memorialPullRequest);
  }

  @Test
  void createMemorialPullRequestFail1() {
    // 커밋 PR 실패(메모리얼 커밋 Not Found)
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto);
    MemorialPullRequestDto memorialPullRequestDto = new MemorialPullRequestDto("test", 2L);
    assertThrows(MemorialCommitNotFoundException.class, () -> memorialCommitService.createMemorialPullRequest(memorialPullRequestDto));
  }

  @Test
  void createMemorialPullRequestFail2() {
    // 커밋 PR 실패(해당 메모리얼 커밋이 이미 풀 리퀘스트 보내짐)
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto);
    MemorialPullRequestDto memorialPullRequestDto1 = new MemorialPullRequestDto("test", 1L);
    MemorialPullRequestDto memorialPullRequestDto2 = new MemorialPullRequestDto("test", 1L);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto1);
    assertThrows(MemorialPullRequestAlreadySentException.class, () -> memorialCommitService.createMemorialPullRequest(memorialPullRequestDto2));
  }


  @Test
  void mergeMemorialCommitCase1() {
    // 이전에 APPROVE 된 PR이 없을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto);
    MemorialPullRequestDto memorialPullRequestDto = new MemorialPullRequestDto("test", 1L);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto);
    MemorialMergeRequestDto memorialMergeRequestDto = new MemorialMergeRequestDto("test", 1L);
    memorialCommitService.mergeMemorialCommit(memorialMergeRequestDto);
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(1L)
            .orElse(null);
    assertNotNull(memorialPullRequest);
    assertEquals(MemorialPullRequestState.APPROVED, memorialPullRequest.getState());
  }

  @Test
  void mergeMemorialCommitCase2() {
    // 이전에 APPROVED 된 PR이 있을 때
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto memorialCommitRequestDto1 = new MemorialCommitRequestDto("test", 1L, "test");
    MemorialCommitRequestDto memorialCommitRequestDto2 = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto1);
    memorialCommitService.createMemorialCommit(memorialCommitRequestDto2);

    MemorialPullRequestDto memorialPullRequestDto1 = new MemorialPullRequestDto("test1", 1L);
    MemorialPullRequestDto memorialPullRequestDto2 = new MemorialPullRequestDto("test2", 2L);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto1);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialCommitService.mergeMemorialCommit(memorialMergeRequestDto1);
    memorialCommitService.mergeMemorialCommit(memorialMergeRequestDto2);

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

    MemorialPullRequestDto memorialPullRequestDto1 = new MemorialPullRequestDto("test1", 1L);
    MemorialPullRequestDto memorialPullRequestDto2 = new MemorialPullRequestDto("test2", 2L);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto1);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialCommitService.mergeMemorialCommit(memorialMergeRequestDto1);
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialCommitService.validateMergeable(memorialMergeRequestDto2);

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

    MemorialPullRequestDto memorialPullRequestDto1 = new MemorialPullRequestDto("test1", 1L);
    MemorialPullRequestDto memorialPullRequestDto2 = new MemorialPullRequestDto("test2", 2L);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto1);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialCommitService.mergeMemorialCommit(memorialMergeRequestDto1);
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialCommitService.validateMergeable(memorialMergeRequestDto2);

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

    MemorialPullRequestDto memorialPullRequestDto1 = new MemorialPullRequestDto("test1", 1L);
    MemorialPullRequestDto memorialPullRequestDto2 = new MemorialPullRequestDto("test2", 2L);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto1);
    memorialCommitService.createMemorialPullRequest(memorialPullRequestDto2);

    MemorialMergeRequestDto memorialMergeRequestDto1 = new MemorialMergeRequestDto("test", 1L);
    MemorialMergeRequestDto memorialMergeRequestDto2 = new MemorialMergeRequestDto("test", 2L);
    memorialCommitService.mergeMemorialCommit(memorialMergeRequestDto1);
    MemorialMergeableResponseDto memorialMergeableResponseDto = memorialCommitService.validateMergeable(memorialMergeRequestDto2);

    assertNotNull(memorialMergeableResponseDto);
    assertEquals(false, memorialMergeableResponseDto.mergeable());
  }

}
