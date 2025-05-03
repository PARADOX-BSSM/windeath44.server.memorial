package windeath44.server.memorial.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.entity.MemorialPullRequest;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;
import windeath44.server.memorial.domain.entity.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.presentation.dto.request.MemorialRejectRequestDto;

@Service
@RequiredArgsConstructor
public class MemorialRejectService {
  private final MemorialPullRequestRepository memorialPullRequestRepository;

  public void rejectMemorialPullRequest(MemorialRejectRequestDto memorialRejectRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialRejectRequestDto.memorialPullRequestId())
            .orElseThrow((MemorialPullRequestNotFoundException::new));
    memorialPullRequest.reject(memorialRejectRequestDto.userId());

    memorialPullRequestRepository.save(memorialPullRequest);
  }
}
