package windeath44.server.memorial.domain.memorial.exception;

public class AuthenticationFailedException extends RuntimeException {
  public AuthenticationFailedException() {
    super("Authentication Failed");
  }
}
