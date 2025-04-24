package windeath44.server.memorial.domain.presentation.dto.response;

import windeath44.server.memorial.domain.entity.Memorial;
import windeath44.server.memorial.domain.entity.MemorialCommit;
import windeath44.server.memorial.domain.entity.MemorialPullRequestState;

import java.time.LocalDateTime;

public record MemorialPullRequestResponseDto(
  Long memorialPullRequestId,
  String userId,
  MemorialCommit memorialCommit,
  Memorial memorial,
  MemorialPullRequestState state,
  LocalDateTime updatedAt
) {
}
