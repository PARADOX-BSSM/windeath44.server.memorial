package windeath44.server.memorial.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record MemorialResponseDto(
        Long memorialId,
        Long characterId,
        List<String> chiefs,
        Long bowCount,
        Long memorialCommitId,
        String content,
        String userId,
        LocalDateTime createdAt,
        String mergerId,
        LocalDateTime updatedAt
) {
}
