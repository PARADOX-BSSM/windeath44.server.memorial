package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialMergeConflictException extends GlobalException {
  public MemorialMergeConflictException() {
    super(ErrorCode.MEMORIAL_MERGE_CONFLICT);
  }
}