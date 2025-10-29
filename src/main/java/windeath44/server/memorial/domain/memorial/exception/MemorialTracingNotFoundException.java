package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialTracingNotFoundException extends GlobalException {
  public MemorialTracingNotFoundException() {
    super(ErrorCode.MEMORIAL_TRACING_NOT_FOUND);
  }
}
