package windeath44.server.memorial.domain.memorial.dto.response;

import com.querydsl.core.Tuple;

public record TodayMemorialResponse(
        Long memorialId,
        Long characterId
) {
}
