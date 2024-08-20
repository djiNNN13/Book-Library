package com.example.booklibrary.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReaderWithBooksDto {
  private Long id;
  private String name;
  private List<BookDto> books;
}
