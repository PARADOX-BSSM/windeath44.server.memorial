package windeath44.server.memorial.domain.memorial.dto.response;

import com.querydsl.core.types.dsl.NumberPath;

public record MemorialListResponseDto(
        Long memorialId,
        Long characterId
) {
}
