package windeath44.server.memorial.domain.presentation.dto.response;

import windeath44.server.memorial.domain.entity.MemorialPullRequestState;

import java.time.LocalDateTime;

public record MemorialCommitResponseDto(
        Long memorialCommitId,
        String userId,
        Long memorialId,
        String content,
        LocalDateTime createdAt
) {
}
