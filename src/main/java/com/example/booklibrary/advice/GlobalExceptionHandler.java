package com.example.booklibrary.advice;

import com.example.booklibrary.util.ErrorDetail;
import com.example.booklibrary.util.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;
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
            "Failed to create a new book, the request contains invalid fields",
            errors);

    return ResponseEntity.badRequest().body(errorResponse);
  }
}
