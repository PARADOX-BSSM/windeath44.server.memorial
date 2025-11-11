package windeath44.server.memorial.domain.memorial.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class BowedWithin24HoursException extends RuntimeException {
  private String remainTime;
  public BowedWithin24HoursException(String dateTime) {
    super("You can only bow once every 24 hours.");
    this.remainTime = dateTime;
  }
}
