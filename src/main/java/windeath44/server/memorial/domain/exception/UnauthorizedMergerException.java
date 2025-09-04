package windeath44.server.memorial.domain.exception;

public class UnauthorizedMergerException extends RuntimeException {
  public UnauthorizedMergerException() {
    super("Only authorized mergers(chiefs) can merge.");
  }
}
