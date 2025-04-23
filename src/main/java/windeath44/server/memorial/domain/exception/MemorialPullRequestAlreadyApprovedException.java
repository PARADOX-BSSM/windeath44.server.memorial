package windeath44.server.memorial.domain.exception;

public class MemorialPullRequestAlreadyApprovedException extends RuntimeException {
  public MemorialPullRequestAlreadyApprovedException() {
    super("Memorial pull request is already approved.");
  }
}
