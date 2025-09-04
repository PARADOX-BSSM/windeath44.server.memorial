package windeath44.server.memorial.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import windeath44.server.memorial.domain.exception.*;
import windeath44.server.memorial.global.dto.ErrorResponseDto;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
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

  @ExceptionHandler(UnauthorizedMergerException.class)
  protected ResponseEntity<ErrorResponseDto> handleUnauthorizedMergerException(UnauthorizedMergerException e) {
    log.error(e.getMessage(), e);
    final ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
    return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
  }
}
