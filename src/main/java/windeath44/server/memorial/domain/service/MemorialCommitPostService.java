package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.domain.Memorial;
import windeath44.server.memorial.domain.domain.MemorialCommit;
import windeath44.server.memorial.domain.domain.MemorialCommitState;
import windeath44.server.memorial.domain.domain.MemorialPullRequest;
import windeath44.server.memorial.domain.domain.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.domain.repository.MemorialRepository;
import windeath44.server.memorial.domain.domain.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;
import windeath44.server.memorial.domain.service.mapper.MemorialCommitMapper;

@Service
@RequiredArgsConstructor
public class MemorialCommitPostService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
  private final MemorialRepository memorialRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;

  public void createMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto) {
    Memorial memorial = memorialRepository.findById(memorialCommitRequestDto.memorialId())
            .orElseThrow();
    memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(memorialCommitRequestDto, memorial));
  }

  public void createMemorialPullRequest(MemorialPullRequestDto memorialPullRequestDto) {
    System.out.println(memorialPullRequestDto);
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialPullRequestDto.memorialCommitId())
            .orElseThrow();
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    if (memorialPullRequestExists != null) {
      return;
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, memorialPullRequestDto.userId());
    memorialPullRequestRepository.save(memorialPullRequest);
  }

  public void mergeMemorialCommit(MemorialMergeRequestDto memorialMergeRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialMergeRequestDto.memorialCommitId())
            .orElseThrow();
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    memorialPullRequest.approve();
    memorialPullRequest.merger(memorialMergeRequestDto.userId());
    MemorialPullRequest latestApprovedMemorialPullRequest = memorialPullRequestRepository.findMemorialPullRequestByMemorialAndState(memorial, MemorialCommitState.APPROVED);
    if (latestApprovedMemorialPullRequest != null) {
      latestApprovedMemorialPullRequest.store();
      memorialPullRequestRepository.save(latestApprovedMemorialPullRequest);
    }
    memorialPullRequestRepository.save(memorialPullRequest);
  }
}
