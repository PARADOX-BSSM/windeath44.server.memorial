package windeath44.server.memorial.domain.dto.response;

import java.time.LocalDateTime;

public record MemorialBowResponseDto(
        Long bowId,
        Long memorialId,
        String userId,
        Long bowCount,
        LocalDateTime lastBowedAt
) {
}
