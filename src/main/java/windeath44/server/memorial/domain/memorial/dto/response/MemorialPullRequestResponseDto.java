package windeath44.server.memorial.domain.memorial.dto.response;

import windeath44.server.memorial.domain.memorial.model.MemorialPullRequestState;

import java.time.LocalDateTime;

public record MemorialPullRequestResponseDto(
  Long memorialPullRequestId,
  String userId,
  MemorialCommitResponseDto memorialCommit,
  MemorialSimpleResponseDto memorial,
  MemorialPullRequestState state,
  LocalDateTime updatedAt
) {
}
