package windeath44.server.memorial.global.error;
import windeath44.server.memorial.global.error.exception.ErrorCode;

public record ErrorResponse (
        int status,
        String message
) {
  public ErrorResponse(ErrorCode errorCode) {
    this(
            errorCode.getStatus(),
            errorCode.getMessage()
    );
  }
}
