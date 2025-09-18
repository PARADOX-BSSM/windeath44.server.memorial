package windeath44.server.memorial.domain.dto.response;

import windeath44.server.memorial.domain.model.MemorialPullRequestState;

import java.time.LocalDateTime;

public record MemorialPullRequestResponseDto(
  Long memorialPullRequestId,
  String userId,
  MemorialCommitResponseDto memorialCommit,
  MemorialPullRequestState state,
  LocalDateTime updatedAt
) {
}
