package windeath44.server.memorial.domain.memorial.dto.response;

import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialCommit;
import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;

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
