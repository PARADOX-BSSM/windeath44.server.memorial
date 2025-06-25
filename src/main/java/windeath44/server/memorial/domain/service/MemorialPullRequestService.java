package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.mapper.MemorialCommitMapper;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemorialPullRequestService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
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
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest, memorialCommitMapper.toMemorialCommitResponseDto(memorialCommit, memorial));
  }

  public MemorialPullRequestResponseDto findMemorialPullRequestById(Long memorialPullRequestId) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialPullRequestId)
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    System.out.println(memorialCommitMapper.toMemorialCommitResponseDto(memorialPullRequest.getMemorialCommit(), memorialPullRequest.getMemorial()).memorialId());
    return memorialPullRequestMapper.toMemorialPullRequestResponseDto(memorialPullRequest, memorialCommitMapper.toMemorialCommitResponseDto(memorialPullRequest.getMemorialCommit(), memorialPullRequest.getMemorial()));
  }

  public List<MemorialPullRequestResponseDto> findMemorialPullRequestsByMemorialId(Long memorialId) {
    List<MemorialPullRequest> memorialPullRequests = memorialPullRequestRepository.findMemorialPullRequestsByMemorial_MemorialId(memorialId);
    return memorialPullRequests.stream().map(x -> memorialPullRequestMapper.toMemorialPullRequestResponseDto(x, memorialCommitMapper.toMemorialCommitResponseDto(x.getMemorialCommit(), x.getMemorial()))).toList();
  }

  @Transactional
  public void deleteMemorialPullRequestsByMemorialId(Long memorialId) {
    List<MemorialPullRequest> memorialPullRequests = memorialPullRequestRepository.findMemorialPullRequestsByMemorial_MemorialId(memorialId);
    memorialPullRequestRepository.deleteAll(memorialPullRequests);
  }
}
