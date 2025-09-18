package windeath44.server.memorial.global.dto;

public record ResponseDto<T> (
        String message,
        T data
) {
}
