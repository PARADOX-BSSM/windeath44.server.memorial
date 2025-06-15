package windeath44.server.memorial.global.dto;

import java.util.List;

public record CursorPage<T> (
        Boolean hasNext,
        List<T> data
) {
}
