package windeath44.server.memorial.domain.presentation.dto.global;

public record ErrorResponseDto(
        Integer status,
        String message,
        Object data
) {
}
