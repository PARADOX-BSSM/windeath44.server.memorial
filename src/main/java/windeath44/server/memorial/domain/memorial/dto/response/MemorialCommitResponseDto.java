package windeath44.server.memorial.domain.memorial.dto.response;

import java.time.LocalDateTime;

public record MemorialCommitResponseDto(
        Long memorialCommitId,
        String userId,
        Long memorialId,
        String content,
        LocalDateTime createdAt
) {
}
