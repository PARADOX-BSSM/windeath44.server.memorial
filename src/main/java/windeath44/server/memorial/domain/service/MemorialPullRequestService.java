package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialCommit;
import windeath44.server.memorial.domain.model.MemorialPullRequest;
import windeath44.server.memorial.domain.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.exception.MemorialPullRequestAlreadySentException;
import windeath44.server.memorial.domain.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.mapper.MemorialPullRequestMapper;
import windeath44.server.memorial.domain.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialPullRequestResponseDto;

@Service
@RequiredArgsConstructor
public class MemorialPullRequestService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final MemorialPullRequestMapper memorialPullRequestMapper;

  @Transactional
  public MemorialPullRequestResponseDto createMemorialPullRequest(MemorialPullRequestRequestDto memorialPullRequestRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialPullRequestRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    if (memorialPullRequestExists != null) {
      MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, memorialPullRequestRequestDto.userId(), MemorialPullRequestState.REJECTED);
      memorialPullRequestRepository.save(memorialPullRequest);
      throw new MemorialPullRequestAlreadySentException();
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, memorialPullRequestRequestDto.userId());
    memorialPullRequestRepository.save(memorialPullRequest);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }

  public MemorialPullRequestResponseDto findMemorialPullRequestById(Long memorialPullRequestId) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialPullRequestId)
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }
}
