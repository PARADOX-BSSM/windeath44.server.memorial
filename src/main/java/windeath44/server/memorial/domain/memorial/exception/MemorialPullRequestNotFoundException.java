package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialPullRequestNotFoundException extends GlobalException {
  public MemorialPullRequestNotFoundException() {
    super(ErrorCode.MEMORIAL_PULL_REQUEST_NOT_FOUND);
  }
}