package windeath44.server.memorial.domain.presentation.dto.response;

import windeath44.server.memorial.domain.domain.MemorialCommitState;

import java.time.LocalDateTime;

public record MemorialCommitResponseDto(
        Long memorial_commit_id,
        String user_id,
        Long memorial_id,
        String content,
        MemorialCommitState state,
        LocalDateTime created_at
) {
}
