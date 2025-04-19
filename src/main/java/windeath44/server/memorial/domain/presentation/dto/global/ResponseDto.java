package windeath44.server.memorial.domain.presentation.dto.global;

public record ResponseDto(
        Long status,
        String message,
        Object data
) {
}
