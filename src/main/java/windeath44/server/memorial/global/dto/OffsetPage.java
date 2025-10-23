package windeath44.server.memorial.global.dto;

import java.util.List;

public record OffsetPage<T> (
        int total,
        List<T> values
) {

    public static <T> OffsetPage<T> of(int total, List<T> values) {
        return new OffsetPage<>(total, values);
    }
}
