package windeath44.server.memorial.domain.anime.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class AlreadyCachedAnimeException extends GlobalException {

  public AlreadyCachedAnimeException() {
    super(ErrorCode.ANIME_ALREADY_CACHED);
  }

  class Holder {
    private static final AlreadyCachedAnimeException INSTANCE = new AlreadyCachedAnimeException();
  }

  public static AlreadyCachedAnimeException getInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public String getMessage() {
    return this.getErrorCode().getMessage();
  }

}
