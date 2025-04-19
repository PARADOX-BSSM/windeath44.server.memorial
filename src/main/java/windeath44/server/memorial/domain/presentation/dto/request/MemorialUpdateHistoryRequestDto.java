package windeath44.server.memorial.domain.presentation.dto.request;

import java.time.LocalDateTime;

public record MemorialUpdateHistoryRequestDto(
        String userId,
        Long memorialCommitId
) {
}
