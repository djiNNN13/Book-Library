package com.example.booklibrary.dto;

import java.util.Objects;

public class BookDto {
  private Long id;
  private String name;
  private String author;

  public BookDto() {}

  public BookDto(Long id, String name, String author) {
    this.id = id;
    this.name = name;
    this.author = author;
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

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BookDto bookDto = (BookDto) o;
    return Objects.equals(id, bookDto.id)
        && Objects.equals(name, bookDto.name)
        && Objects.equals(author, bookDto.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, author);
  }
}
