package windeath44.server.memorial.domain.exception;

public class MemorialCommentNotFoundException extends RuntimeException{
  public MemorialCommentNotFoundException() {
    super("Memorial Comment Not Found");
  }
}
