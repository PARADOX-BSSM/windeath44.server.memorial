package windeath44.server.memorial.domain.exception;

public class MemorialNotFoundException extends RuntimeException {
  public MemorialNotFoundException() {
    super("Memorial Not Found");
  }
}
