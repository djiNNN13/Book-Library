package com.example.booklibrary.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
  private LocalDateTime localDateTime;
  private String errorMessage;
  private List<ErrorDetail> errors;
}
