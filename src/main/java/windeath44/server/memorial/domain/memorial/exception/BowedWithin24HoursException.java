package windeath44.server.memorial.domain.memorial.exception;

import java.time.LocalDateTime;

public class BowedWithin24HoursException extends RuntimeException {
  public BowedWithin24HoursException(LocalDateTime dateTime) {
    super("You can only bow once every 24 hours. Remain Time: " + dateTime.toString());
  }
}
