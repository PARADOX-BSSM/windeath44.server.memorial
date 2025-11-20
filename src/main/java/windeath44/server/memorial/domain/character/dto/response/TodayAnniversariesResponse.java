package windeath44.server.memorial.domain.character.dto.response;

import java.util.List;

public record TodayAnniversariesResponse(
        List<Long> characterIds
) {
}
