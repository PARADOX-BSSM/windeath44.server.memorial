package windeath44.server.memorial.domain.exception;

public class MemorialPullRequestNotFoundException extends RuntimeException {
  public MemorialPullRequestNotFoundException() {
    super("Memorial pull request not found.");
  }
}
