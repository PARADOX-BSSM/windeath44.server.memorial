package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class InvalidCursorFormatException extends GlobalException {
  public InvalidCursorFormatException() {
    super(ErrorCode.INVALID_CURSOR_FORMAT);
  }
}