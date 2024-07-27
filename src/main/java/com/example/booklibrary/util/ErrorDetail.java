package com.example.booklibrary.util;
import org.springframework.validation.FieldError;

public class ErrorDetail {
  private final String field;

  private final String message;

  private final String rejectedValue;

  public ErrorDetail(FieldError fieldError) {
    this.field = fieldError.getField();
    this.message = fieldError.getDefaultMessage();
    this.rejectedValue = (fieldError.getRejectedValue() != null) ? fieldError.getRejectedValue().toString() : "null";
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }

  public Object getRejectedValue() {
    return rejectedValue;
  }
}
