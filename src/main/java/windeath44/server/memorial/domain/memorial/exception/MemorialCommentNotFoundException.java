package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialCommentNotFoundException extends GlobalException {
  public MemorialCommentNotFoundException() {
    super(ErrorCode.MEMORIAL_COMMENT_NOT_FOUND);
  }
}