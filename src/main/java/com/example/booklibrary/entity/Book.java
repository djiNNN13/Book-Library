package com.example.booklibrary.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
  @Positive
  @Schema(description = "ID of the book", type = "Long", example = "1")
  private Long id;

  @NotBlank(message = "Book name cannot be null!")
  @Pattern(
      regexp = "^[^|/\\\\#%=+*_><]*$",
      message =
          "Book name must not contain the following characters: |/\\#%=+*_><, and must be written using ENGLISH letters")
  @Length(
      min = 5,
      max = 100,
      message = "Book name must be longer than 5 characters, shorter than 100 characters")
  @Schema(description = "Name of the book", type = "String", example = "Jack London")
  private String name;

  @NotBlank(message = "Book author cannot be null!")
  @Pattern(
      regexp = "^[a-zA-Z\\s'-]+$",
      message = "Book author must contain only ENGLISH letters, spaces, dashes, apostrophes")
  @Length(
      min = 5,
      max = 30,
      message = "Book author must be longer than 5 characters, shorter than 30 characters")
  @Schema(description = "Author of the book", type = "String", example = "Martin Eden")
  private String author;

  @Schema(description = "ID of the borrowed reader", type = "Long", example = "2")
  private Long readerId;
}
