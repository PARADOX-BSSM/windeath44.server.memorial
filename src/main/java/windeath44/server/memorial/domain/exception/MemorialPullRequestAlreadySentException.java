package windeath44.server.memorial.domain.exception;

public class MemorialPullRequestAlreadySentException extends RuntimeException {
  public MemorialPullRequestAlreadySentException() {
    super("Memorial Pull Request Already Sent");
  }
}
