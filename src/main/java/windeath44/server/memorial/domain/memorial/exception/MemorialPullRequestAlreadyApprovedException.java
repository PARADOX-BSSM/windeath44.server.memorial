package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialPullRequestAlreadyApprovedException extends GlobalException {
  public MemorialPullRequestAlreadyApprovedException() {
    super(ErrorCode.MEMORIAL_PULL_REQUEST_ALREADY_APPROVED);
  }
}