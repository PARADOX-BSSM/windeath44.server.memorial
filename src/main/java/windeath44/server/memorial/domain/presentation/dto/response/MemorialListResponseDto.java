package windeath44.server.memorial.domain.presentation.dto.response;

import com.querydsl.core.types.dsl.NumberPath;

public record MemorialListResponseDto(
        Long memorialId,
        Long characterId
) {
}
