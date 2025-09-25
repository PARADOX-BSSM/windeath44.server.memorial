package windeath44.server.memorial.domain.memorial.dto.request;

import java.util.List;

public record MemorialCharacterFilterRequestDto(
        List<Long> characters,
        String orderBy,
        Long page
) {
}
