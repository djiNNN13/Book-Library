package com.example.booklibrary.dto;

import com.example.booklibrary.entity.Book;

import java.util.List;

public class ReaderWithBooksDto {
  private Long id;
  private String name;
  private List<BookDto> books;

  public ReaderWithBooksDto(Long id, String name, List<BookDto> books) {
    this.id = id;
    this.name = name;
    this.books = books;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<BookDto> getBooks() {
    return books;
  }

  public void setBooks(List<BookDto> books) {
    this.books = books;
  }
}
