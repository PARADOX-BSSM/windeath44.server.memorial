package windeath44.server.memorial.global.error;

import windeath44.server.memorial.domain.memorial.exception.*;
import windeath44.server.memorial.global.dto.ErrorResponseDto;
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

  private void errorLogging(ErrorCode errorCode, GlobalException e) {
    log.error("error message : {} || error status : {}", errorCode.getMessage(), errorCode.getStatus());
    log.error(e.getStackTrace().toString());
  }

  @ExceptionHandler(MemorialNotFoundException.class)
  protected ResponseEntity<ErrorResponseDto> handleMemorialNotFoundException(MemorialNotFoundException e) {
    log.error(e.getMessage(), e);
    final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
    return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MemorialCommitNotFoundException.class)
  protected ResponseEntity<ErrorResponseDto> handleMemorialCommitNotFoundException(MemorialCommitNotFoundException e) {
    log.error(e.getMessage(), e);
    final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
    return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MemorialPullRequestAlreadyApprovedException.class)
  protected ResponseEntity<ErrorResponseDto> handleMemorialPullRequestAlreadyApprovedException(MemorialPullRequestAlreadyApprovedException e) {
    log.error(e.getMessage(), e);
    final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
    return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MemorialPullRequestAlreadySentException.class)
  protected ResponseEntity<ErrorResponseDto> handleMemorialPullRequestAlreadySentException(MemorialPullRequestAlreadySentException e) {
    log.error(e.getMessage(), e);
    final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
    return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MemorialPullRequestNotFoundException.class)
  protected ResponseEntity<ErrorResponseDto> handleMemorialPullRequestNotFoundException(MemorialPullRequestNotFoundException e) {
    log.error(e.getMessage(), e);
    final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
    return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UndefinedOrderByException.class)
  protected ResponseEntity<ErrorResponseDto> handleUndefinedOrderByException(UndefinedOrderByException e) {
    log.error(e.getMessage(), e);
    final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
    return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
  }

}
