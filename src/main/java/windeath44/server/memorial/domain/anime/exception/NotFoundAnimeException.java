package windeath44.server.memorial.domain.anime.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class NotFoundAnimeException extends GlobalException {
  public NotFoundAnimeException() {
    super(ErrorCode.ANIME_NOT_FOUND);
  }

  private static class Holder {
    private static final NotFoundAnimeException INSTANCE = new NotFoundAnimeException();
  }

  public static NotFoundAnimeException getInstance() {
    return Holder.INSTANCE;
  }

}