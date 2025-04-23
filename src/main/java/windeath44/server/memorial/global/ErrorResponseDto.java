package windeath44.server.memorial.global;

public record ErrorResponseDto(
        String detail,
        Object data
) {

  public ErrorResponseDto(String detail) {
    this(detail, null);
  }
}
