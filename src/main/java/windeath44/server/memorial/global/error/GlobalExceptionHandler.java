package windeath44.server.memorial.global.error;

import windeath44.server.memorial.domain.memorial.exception.BowedWithin24HoursException;
import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ErrorResponse> customGlobalExceptionHandler(GlobalException e) {
    ErrorCode errorCode = e.getErrorCode();
    int status = errorCode.getStatus();
    errorLogging(errorCode, e);
    return new ResponseEntity<>(
            new ErrorResponse(errorCode),
            HttpStatus.valueOf(status)
    );
  }

  @ExceptionHandler(BowedWithin24HoursException.class)
  public ResponseEntity<BowedWithin24HoursExceptionResponse> bowedWithin24HoursExceptionHandler(BowedWithin24HoursException e) {
    log.error("error message : {}", e.getMessage());
    return new ResponseEntity<>(
            BowedWithin24HoursExceptionResponse.from(ErrorCode.BOWED_WITHIN_24_HOURS, e.getRemainTime()),
            HttpStatus.valueOf(ErrorCode.BOWED_WITHIN_24_HOURS.getStatus())
    );
  }
  
  private void errorLogging(ErrorCode errorCode, GlobalException e) {
    log.error("error message : {} || error status : {}", errorCode.getMessage(), errorCode.getStatus());
    log.error(e.getStackTrace().toString());
  }
}
