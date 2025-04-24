package windeath44.server.memorial.domain.exception;

public class MemorialCommitNotFoundException extends RuntimeException {
  public MemorialCommitNotFoundException() {
    super("Memorial Commit Not Found");
  }
}
