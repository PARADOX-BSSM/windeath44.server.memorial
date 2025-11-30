package windeath44.server.memorial.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ANIME_NOT_FOUND(404, "Anime not found"),
  ANIME_ALREADY_CACHED(500, "Anime already cached"),
  CHARACTER_NOT_FOUND(404, "Character not found"),
  CHARACTER_CHANGE_REQUEST_NOT_FOUND(404, "Character change request not found"),
  FILE_UPLOAD_FAILED(500, "File upload failed"),
  CHARACTER_CAUSE_OF_DEATH_CASTING_FAILED(400, "Character cause of death casting failed"),

  MEMORIAL_NOT_FOUND(404, "Memorial Not Found"),
  MEMORIAL_COMMIT_NOT_FOUND(404, "Memorial Commit Not Found"),
  MEMORIAL_COMMENT_NOT_FOUND(404, "Memorial Comment Not Found"),
  MEMORIAL_PULL_REQUEST_NOT_FOUND(404, "Memorial Pull Request Not Found"),
  MEMORIAL_PULL_REQUEST_ALREADY_SENT(409, "Memorial Pull Request Already Sent"),
  MEMORIAL_PULL_REQUEST_ALREADY_APPROVED(409, "Memorial Pull Request Already Approved"),
  AUTHENTICATION_FAILED(401, "Authentication Failed"),
  MEMORIAL_MERGE_PERMISSION_DENIED(403, "Only memorial chiefs can merge commits"),
  UNDEFINED_ORDER_BY(400, "Order By is not defined"),
  INVALID_CURSOR_FORMAT(400, "Invalid cursor format"),
  MEMORIAL_TRACING_NOT_FOUND(404, "Memorial Tracing Not Found"),
  UNAUTHORIZED_MEMORIAL_TRACING_ACCESS(403, "Unauthorized Memorial Tracing Access"),
  BOWED_WITHIN_24_HOURS(403, "You have already bowed to this memorial within the last 24 hours"),
  MEMORIAL_MERGE_CONFLICT(409, "Memorial merge conflict detected"),
  TOP_MEMORIAL_NOT_FOUND(404, "Top Memorial Not Found");
  private int status;
  private String message;
}
