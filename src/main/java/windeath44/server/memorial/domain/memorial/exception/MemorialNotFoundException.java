package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialNotFoundException extends GlobalException {
  public MemorialNotFoundException() {
    super(ErrorCode.MEMORIAL_NOT_FOUND);
  }
}