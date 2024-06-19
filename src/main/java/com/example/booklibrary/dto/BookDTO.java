package com.example.booklibrary.dto;

import com.example.booklibrary.entity.Reader;

public class BookDTO {
  private Long id;
  private String author;
  private String name;
  private Reader reader;

  public BookDTO(Long id, String author, String name, Reader reader) {
    this.id = id;
    this.author = author;
    this.name = name;
    this.reader = reader;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Reader getReader() {
    return reader;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }
}
