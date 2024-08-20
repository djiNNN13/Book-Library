package com.example.booklibrary.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Getter
@Setter
public class ErrorDetail {
  private final String field;

  private final String message;

  private final String rejectedValue;

  public ErrorDetail(FieldError fieldError) {
    this.field = fieldError.getField();
    this.message = fieldError.getDefaultMessage();
    this.rejectedValue =
        (fieldError.getRejectedValue() != null) ? fieldError.getRejectedValue().toString() : "null";
  }
}
