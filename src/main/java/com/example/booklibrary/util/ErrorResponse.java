package com.example.booklibrary.util;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
  private LocalDateTime localDateTime;
  private String errorMessage;
  private List<ErrorDetail> errors;

  public ErrorResponse(LocalDateTime localDateTime, String errorMessage, List<ErrorDetail> errors) {
    this.localDateTime = localDateTime;
    this.errorMessage = errorMessage;
    this.errors = errors;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public List<ErrorDetail> getErrors() {
    return errors;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void setErrors(List<ErrorDetail> errors) {
    this.errors = errors;
  }
}
