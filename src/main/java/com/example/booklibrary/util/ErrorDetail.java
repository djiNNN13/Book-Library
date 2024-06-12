package com.example.booklibrary.util;

import org.springframework.validation.FieldError;

public class ErrorDetail {
  private String field;
  private String message;

  public ErrorDetail(FieldError fieldError) {
    this.field = fieldError.getField();
    this.message = fieldError.getDefaultMessage();
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }

  public void setField(String field) {
    this.field = field;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
