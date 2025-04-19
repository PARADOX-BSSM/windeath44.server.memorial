package windeath44.server.memorial.domain.presentation.dto.global;

public record ResponseDto(
        Integer status,
        String message,
        Object data
) {
}
