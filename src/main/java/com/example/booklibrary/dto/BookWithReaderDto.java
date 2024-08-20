package com.example.booklibrary.dto;

import com.example.booklibrary.entity.Reader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
@Setter
@AllArgsConstructor
public class BookWithReaderDto {
  private Long id;
  private String author;
  private String name;
  private Reader reader;
}
