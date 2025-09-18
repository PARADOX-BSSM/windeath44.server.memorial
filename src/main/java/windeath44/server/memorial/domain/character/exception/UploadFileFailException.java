package windeath44.server.memorial.domain.character.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class UploadFileFailException extends GlobalException {
  public UploadFileFailException() {
    super(ErrorCode.FILE_UPLOAD_FAILED);
  }
  private static class Holder {
    private static final UploadFileFailException INSTANCE = new UploadFileFailException();
  }
  public static UploadFileFailException getInstance() {
    return Holder.INSTANCE;
  }
}
