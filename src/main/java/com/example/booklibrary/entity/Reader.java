package com.example.booklibrary.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reader {
  @Positive
  @Schema(description = "ID of the reader", type = "Long", example = "1")
  private Long id;

  @NotBlank(message = "Reader name cannot be null!")
  @Pattern(
      regexp = "^[a-zA-Z\\s'-]+$",
      message = "Reader name must contain only ENGLISH letters, spaces, dashes, apostrophes")
  @Length(
      min = 5,
      max = 30,
      message = "Reader name must be longer than 5 characters, shorter than 30 characters")
  @Schema(description = "Name of the reader", type = "String", example = "Yevhenii")
  private String name;
}
