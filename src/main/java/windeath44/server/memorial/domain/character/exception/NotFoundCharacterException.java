package windeath44.server.memorial.domain.character.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class NotFoundCharacterException extends GlobalException {

  public NotFoundCharacterException() {
    super(ErrorCode.CHARACTER_NOT_FOUND);
  }

  private static class Holder {
    private final static NotFoundCharacterException INSTANCE = new NotFoundCharacterException();
  }

  public static NotFoundCharacterException getInstance() {
    return Holder.INSTANCE;
  }
}