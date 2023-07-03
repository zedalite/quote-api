package de.zedalite.quotes.exceptions;

import de.zedalite.quotes.data.model.ErrorDetails;
import de.zedalite.quotes.data.model.ValidationErrorDetails;
import de.zedalite.quotes.data.model.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a global controller exception handler for handling various exceptions thrown by controller methods.
 * <p>
 * It provides methods to handle different types of exceptions and return appropriate error details.
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorDetails handleNotFoundException(final ResourceNotFoundException ex) {
    return new ErrorDetails(LocalDateTime.now(), ex.getMessage());
  }

  @ExceptionHandler(ResourceAlreadyExitsException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorDetails handleAlreadyExitsException(final ResourceAlreadyExitsException ex) {
    return new ErrorDetails(LocalDateTime.now(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorDetails handleNotValidException(final MethodArgumentNotValidException ex) {
    final List<Violation> violations = new ArrayList<>();
    final var details = new ValidationErrorDetails(LocalDateTime.now(), "Validation failed", violations);
    for (final var fieldError : ex.getBindingResult().getFieldErrors()) {
      details.violations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    return details;
  }
}
