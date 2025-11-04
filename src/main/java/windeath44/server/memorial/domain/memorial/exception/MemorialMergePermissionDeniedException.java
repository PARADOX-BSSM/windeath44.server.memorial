package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class MemorialMergePermissionDeniedException extends GlobalException {
  public MemorialMergePermissionDeniedException() {
    super(ErrorCode.MEMORIAL_MERGE_PERMISSION_DENIED);
  }
}