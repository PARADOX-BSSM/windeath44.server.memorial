package windeath44.server.memorial.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ANIME_NOT_FOUND(404, "Anime not found"),
  ANIME_ALREADY_CACHED(500, "Anime already cached"),
  CHARACTER_NOT_FOUND(404, "Character not found"),
  FILE_UPLOAD_FAILED(500, "File upload failed"),
  CHARACTER_CAUSE_OF_DEATH_CASTING_FAILED(400, "Character cause of death casting failed"),

  MEMORIAL_NOT_FOUND(404, "Memorial Not Found"),
  MEMORIAL_COMMIT_NOT_FOUND(404, "Memorial Commit Not Found"),
  MEMORIAL_COMMENT_NOT_FOUND(404, "Memorial Comment Not Found"),
  MEMORIAL_PULL_REQUEST_NOT_FOUND(404, "Memorial Pull Request Not Found"),
  MEMORIAL_PULL_REQUEST_ALREADY_SENT(409, "Memorial Pull Request Already Sent"),
  MEMORIAL_PULL_REQUEST_ALREADY_APPROVED(409, "Memorial Pull Request Already Approved"),
  AUTHENTICATION_FAILED(401, "Authentication Failed"),
  UNDEFINED_ORDER_BY(400, "Order By is not defined"),
  BOWED_WITHIN_24_HOURS(403, "You can bow only once within 24 hours"),;
  private int status;
  private String message;
}
