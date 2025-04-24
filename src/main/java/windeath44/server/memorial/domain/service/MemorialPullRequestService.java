package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.entity.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.exception.MemorialPullRequestAlreadySentException;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;

@Service
@RequiredArgsConstructor
public class MemorialPullRequestService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;

  public void createMemorialPullRequest(MemorialPullRequestDto memorialPullRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialPullRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    if (memorialPullRequestExists != null) {
      throw new MemorialPullRequestAlreadySentException();
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, memorialPullRequestDto.userId());
    memorialPullRequestRepository.save(memorialPullRequest);
  }
}
