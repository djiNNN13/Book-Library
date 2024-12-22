package com.example.booklibrary.advice;

import com.example.booklibrary.exception.*;
import com.example.booklibrary.util.ErrorDetail;
import com.example.booklibrary.util.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleInvalidArguments(MethodArgumentNotValidException ex) {
    List<ErrorDetail> errors =
        ex.getBindingResult().getFieldErrors().stream().map(ErrorDetail::new).toList();

    var errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
            String.format(
                "Failed to create a new %s, the request contains invalid fields",
                ex.getObjectName()),
            errors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler({
    BookNotFoundException.class,
    ReaderNotFoundException.class,
    BookNotBorrowedException.class,
    DaoOperationException.class,
    LibraryServiceException.class,
    SaveBookException.class,
    SaveReaderException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequestSituations(RuntimeException ex) {
    var errorResponse =
        ErrorResponse.builder()
            .localDateTime(LocalDateTime.now())
            .errorMessage(ex.getMessage())
            .build();
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handlePathVariablesInvalidArguments(
      ConstraintViolationException ex) {
    var errorResponse =
        ErrorResponse.builder()
            .localDateTime(LocalDateTime.now())
            .errorMessage(
                ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(", ")))
            .build();
    return ResponseEntity.badRequest().body(errorResponse);
  }
}
