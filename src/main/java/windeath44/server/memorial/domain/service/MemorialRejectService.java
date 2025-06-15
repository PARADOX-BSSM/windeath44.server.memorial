package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.model.MemorialPullRequest;
import windeath44.server.memorial.domain.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.dto.request.MemorialRejectRequestDto;

@Service
@RequiredArgsConstructor
public class MemorialRejectService {
  private final MemorialPullRequestRepository memorialPullRequestRepository;

  @Transactional
  public void rejectMemorialPullRequest(MemorialRejectRequestDto memorialRejectRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialRejectRequestDto.memorialPullRequestId())
            .orElseThrow((MemorialPullRequestNotFoundException::new));
    memorialPullRequest.reject();
    memorialPullRequestRepository.save(memorialPullRequest);
  }
}
