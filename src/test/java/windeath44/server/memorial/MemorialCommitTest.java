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
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;
import windeath44.server.memorial.domain.service.MemorialCommitService;

import java.util.Collections;
import java.util.List;

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
  void createMemorialCommit() {
    Memorial memorial = new Memorial(1L);
    memorialRepository.save(memorial);

    MemorialCommitRequestDto dto = new MemorialCommitRequestDto("test", 1L, "test");
    memorialCommitService.createMemorialCommit(dto);

    MemorialCommit memorialCommit = memorialCommitRepository.findById(1L)
            .orElse(null);
    assertNotNull(memorialCommit);
  }
}
