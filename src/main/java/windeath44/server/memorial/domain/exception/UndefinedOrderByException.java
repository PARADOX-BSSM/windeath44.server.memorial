package windeath44.server.memorial.domain.exception;

public class UndefinedOrderByException extends RuntimeException {
  public UndefinedOrderByException() {
    super("Order By is not defined.");
  }
}
