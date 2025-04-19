package windeath44.server.memorial.domain.presentation.dto.global;

public record ErrorResponseDto(
        Long status,
        String message,
        Object data
) {
}
