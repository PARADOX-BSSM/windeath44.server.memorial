package windeath44.server.memorial.domain.presentation.dto.global;

public record ErrorResponseDto(
        String detail,
        Object data
) {

  public ErrorResponseDto(String detail) {
    this(detail, null);
  }
}
