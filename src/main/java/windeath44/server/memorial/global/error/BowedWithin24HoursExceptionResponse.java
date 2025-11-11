package windeath44.server.memorial.global.error;
import windeath44.server.memorial.global.error.exception.ErrorCode;

public record BowedWithin24HoursExceptionResponse (
        int status,
        String message,
        String remainTime
) {
    public static BowedWithin24HoursExceptionResponse from(ErrorCode errorCode, String remainTime) {
        return new BowedWithin24HoursExceptionResponse(
                errorCode.getStatus(),
                errorCode.getMessage(),
                remainTime
        );
    }
}
