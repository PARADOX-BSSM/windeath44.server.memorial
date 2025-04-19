package windeath44.server.memorial.domain.presentation.dto.response;

import windeath44.server.memorial.domain.domain.MemorialCommitState;

import java.time.LocalDateTime;

public record MemorialCommitResponseDto(
        Long memorialCommitId,
        String userId,
        Long memorialId,
        String content,
        MemorialCommitState state,
        LocalDateTime createdAt
) {
}
