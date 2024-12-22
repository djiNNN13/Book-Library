package com.example.booklibrary.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
  private Long id;
  private String name;
  private String author;
}
