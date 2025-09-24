package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class BowedWithin24HoursException extends GlobalException {
  public BowedWithin24HoursException() {
    super(ErrorCode.BOWED_WITHIN_24_HOURS);
  }
}
