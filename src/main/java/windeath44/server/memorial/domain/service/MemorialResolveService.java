package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialPullRequest;
import windeath44.server.memorial.domain.repository.MemorialCommitRepository;
import windeath44.server.memorial.domain.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.dto.request.MemorialCommitRequestDto;
import windeath44.server.memorial.domain.dto.request.MemorialMergeRequestDto;
import windeath44.server.memorial.domain.dto.request.MemorialPullRequestRequestDto;
import windeath44.server.memorial.domain.dto.request.MemorialResolveRequestDto;
import windeath44.server.memorial.domain.dto.response.MemorialCommitResponseDto;
import windeath44.server.memorial.domain.dto.response.MemorialPullRequestResponseDto;

@Service
@RequiredArgsConstructor
public class MemorialResolveService {
  private final MemorialCommitRepository memorialCommitRepository;
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final MemorialCommitService memorialCommitService;
  private final MemorialPullRequestService memorialPullRequestService;
  private final MemorialMergeService memorialMergeService;

  public void resolve(String userId, MemorialResolveRequestDto memorialResolveRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialResolveRequestDto.memorialPullRequestId())
            .orElseThrow(MemorialPullRequestNotFoundException::new);
    Memorial memorial = memorialPullRequest.getMemorial();
    Long memorialId = memorial.getMemorialId();
    String resolved = memorialResolveRequestDto.resolved();
    MemorialCommitRequestDto memorialCommitRequestDto = new MemorialCommitRequestDto(memorialId, resolved);
    MemorialCommitResponseDto memorialCommitResponseDto = memorialCommitService.createMemorialCommit(userId, memorialCommitRequestDto);

    MemorialPullRequestRequestDto memorialPullRequestRequestDto = new MemorialPullRequestRequestDto(memorialCommitResponseDto.memorialCommitId());
    MemorialPullRequestResponseDto memorialPullRequestResponseDto = memorialPullRequestService.createMemorialPullRequest(userId, memorialPullRequestRequestDto);
    MemorialMergeRequestDto memorialMergeRequestDto = new MemorialMergeRequestDto(memorialPullRequestResponseDto.memorialPullRequestId());
    memorialMergeService.mergeMemorialCommit(userId, memorialMergeRequestDto);
  }
}
