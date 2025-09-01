package windeath44.server.memorial.domain.memorial.exception;

public class MemorialNotFoundException extends RuntimeException {
  public MemorialNotFoundException() {
    super("Memorial Not Found");
  }
}
