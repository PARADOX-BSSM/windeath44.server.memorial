package windeath44.server.memorial.domain.memorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequest;
import windeath44.server.memorial.domain.memorial.repository.MemorialPullRequestRepository;
import windeath44.server.memorial.domain.memorial.exception.MemorialPullRequestNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.MemorialMergePermissionDeniedException;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialRejectRequestDto;

@Service
@RequiredArgsConstructor
public class MemorialRejectService {
  private final MemorialPullRequestRepository memorialPullRequestRepository;
  private final MemorialChiefService memorialChiefService;

  @Transactional
  public void rejectMemorialPullRequest(String userId, MemorialRejectRequestDto memorialRejectRequestDto) {
    MemorialPullRequest memorialPullRequest = memorialPullRequestRepository.findById(memorialRejectRequestDto.memorialPullRequestId())
            .orElseThrow((MemorialPullRequestNotFoundException::new));

    // Check if user is a chief of this memorial
    if (!memorialChiefService.getChiefs(memorialPullRequest.getMemorial().getMemorialId()).contains(userId)) {
      throw new MemorialMergePermissionDeniedException();
    }

    memorialPullRequest.reject();
    memorialPullRequestRepository.save(memorialPullRequest);
  }
}
