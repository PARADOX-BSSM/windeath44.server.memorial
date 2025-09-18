package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class AuthenticationFailedException extends GlobalException {
  public AuthenticationFailedException() {
    super(ErrorCode.AUTHENTICATION_FAILED);
  }
}