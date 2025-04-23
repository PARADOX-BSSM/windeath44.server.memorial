package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.entity.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialRepository;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.exception.MemorialCommitNotFoundException;
import windeath44.server.memorial.domain.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.exception.MemorialPullRequestAlreadyApprovedException;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialPullRequestDto;
import windeath44.server.memorial.domain.mapper.MemorialCommitMapper;

@Service
@RequiredArgsConstructor
public class MemorialCommitService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialCommitMapper memorialCommitMapper;
  private final MemorialRepository memorialRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;

  public void createMemorialCommit(MemorialCommitRequestDto memorialCommitRequestDto) {
    Memorial memorial = memorialRepository.findById(memorialCommitRequestDto.memorialId())
            .orElseThrow(MemorialNotFoundException::new);
    memorialCommitRepository.save(memorialCommitMapper.toMemorialCommit(memorialCommitRequestDto, memorial));
  }

  public void createMemorialPullRequest(MemorialPullRequestDto memorialPullRequestDto) {
    MemorialCommit memorialCommit = memorialCommitRepository.findById(memorialPullRequestDto.memorialCommitId())
            .orElseThrow(MemorialCommitNotFoundException::new);
    Memorial memorial = memorialCommit.getMemorial();

    MemorialPullRequest memorialPullRequestExists = memorialPullRequestRepository.findByMemorialCommit(memorialCommit);
    if (memorialPullRequestExists != null) {
      return;
    }

    MemorialPullRequest memorialPullRequest = new MemorialPullRequest(memorialCommit, memorial, memorialPullRequestDto.userId());
    memorialPullRequestRepository.save(memorialPullRequest);
  }

  public void mergeMemorialCommit(MemorialMergeRequestDto memorialMergeRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialMergeRequestDto.memorialPullRequestId())
            .orElseThrow(MemorialNotFoundException::new);
    Memorial memorial = memorialPullRequest.getMemorial();

    if(memorialPullRequest.isAlreadyApproved())
      throw new MemorialPullRequestAlreadyApprovedException();
    
    MemorialPullRequest latestApprovedMemorialPullRequest = memorialPullRequestRepository.findMemorialPullRequestByMemorialAndState(memorial, MemorialPullRequestState.APPROVED);
    memorialPullRequest.approve();
    memorialPullRequest.merger(memorialMergeRequestDto.userId());

    if (latestApprovedMemorialPullRequest != null) {
      latestApprovedMemorialPullRequest.store();
      memorialPullRequestRepository.save(latestApprovedMemorialPullRequest);
    }

    memorialPullRequestRepository.save(memorialPullRequest);
  }
}
