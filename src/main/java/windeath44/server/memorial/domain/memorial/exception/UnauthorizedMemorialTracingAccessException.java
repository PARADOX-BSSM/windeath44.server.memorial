package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class UnauthorizedMemorialTracingAccessException extends GlobalException {
  public UnauthorizedMemorialTracingAccessException() {
    super(ErrorCode.UNAUTHORIZED_MEMORIAL_TRACING_ACCESS);
  }
}
