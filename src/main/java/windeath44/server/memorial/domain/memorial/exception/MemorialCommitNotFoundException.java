package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialCommitNotFoundException extends GlobalException {
  public MemorialCommitNotFoundException() {
    super(ErrorCode.MEMORIAL_COMMIT_NOT_FOUND);
  }
}