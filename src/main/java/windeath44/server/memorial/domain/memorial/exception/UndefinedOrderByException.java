package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class UndefinedOrderByException extends GlobalException {
  public UndefinedOrderByException() {
    super(ErrorCode.UNDEFINED_ORDER_BY);
  }
}