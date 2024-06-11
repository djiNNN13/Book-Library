package com.example.booklibrary.advice;

import com.example.booklibrary.util.ErrorDetail;
import com.example.booklibrary.util.ErrorResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private ErrorResponse handleInvalidArguments(MethodArgumentNotValidException ex) {
    List<ErrorDetail> errors = new ArrayList<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(new ErrorDetail(error.getField(), error.getDefaultMessage()));
    }

    return new ErrorResponse(
        LocalDateTime.now(),
        "Failed to create a new book, the request contains invalid fields",
        errors);
  }
}
