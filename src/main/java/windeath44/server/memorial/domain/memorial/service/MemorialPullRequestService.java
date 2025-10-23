package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;
import windeath44.server.memorial.domain.memorial.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.memorial.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestAlreadySentException;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.memorial.mapper.MemorialPullRequestMapper;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.memorial.dto.response.MemorialPullRequestResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemorialPullRequestService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final MemorialPullRequestMapper memorialPullRequestMapper;

  @Transactional
  public MemorialPullRequestResponseDto createMemorialPullRequest(String userId, MemorialPullRequestRequestDto memorialPullRequestRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialPullRequestRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    if (memorialPullRequestExists != null) {
      MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, userId, MemorialPullRequestState.REJECTED);
      memorialPullRequestRepository.save(memorialPullRequest);
      throw new MemorialPullRequestAlreadySentException();
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, userId, MemorialPullRequestState.PENDING);
    memorialPullRequestRepository.save(memorialPullRequest);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }

  @Transactional
  public MemorialPullRequestResponseDto createMemorialPullRequestApproved(String userId, MemorialPullRequestRequestDto memorialPullRequestRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialPullRequestRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    if (memorialPullRequestExists != null) {
      MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, userId, MemorialPullRequestState.REJECTED);
      memorialPullRequestRepository.save(memorialPullRequest);
      throw new MemorialPullRequestAlreadySentException();
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, userId, MemorialPullRequestState.APPROVED);
    memorialPullRequestRepository.save(memorialPullRequest);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }

  public MemorialPullRequestResponseDto findMemorialPullRequestById(Long memorialPullRequestId) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialPullRequestId)
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest);
  }

  @Transactional
  public void deleteMemorialPullRequestsByMemorialId(Long memorialId) {
    List<MemorialPullRequest> memorialPullRequests = memorialPullRequestRepository.findMemorialPullRequestsByMemorial_MemorialId(memorialId);
    memorialPullRequestRepository.deleteAll(memorialPullRequests);
  }

  public List<MemorialPullRequestResponseDto> findMemorialPullRequestsByMemorialId(Long memorialId) {
    List<MemorialPullRequest> memorialPullRequests = memorialPullRequestRepository.findMemorialPullRequestsByMemorial_MemorialId(memorialId);
    return memorialPullRequests.stream()
            .map(memorialPullRequestMapper::toMemorialPullRequestResponseDto)
            .toList();
  }
}
