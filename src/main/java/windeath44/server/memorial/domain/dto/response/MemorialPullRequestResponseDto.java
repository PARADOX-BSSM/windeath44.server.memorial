package windeath44.server.memorial.domain.dto.response;

import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.MemorialCommit;
import windeath44.server.memorial.domain.model.MemorialPullRequestState;

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
