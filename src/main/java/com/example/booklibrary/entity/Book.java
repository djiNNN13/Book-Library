package com.example.booklibrary.entity;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public class Book {
  @Positive private Long id;

  @NotBlank(message = "Book name cannot be null!")
  @Pattern(
      regexp = "^[^|/\\\\#%=+*_><]*$",
      message =
          "Book name must not contain the following characters: |/\\#%=+*_><, and must be written using ENGLISH letters")
  @Length(
      min = 5,
      max = 100,
      message = "Book name must be longer than 5 characters, shorter than 100 characters")
  private String name;

  @NotBlank(message = "Book author cannot be null!")
  @Pattern(
      regexp = "^[a-zA-Z\\s'-]+$",
      message = "Book author must contain only ENGLISH letters, spaces, dashes, apostrophes")
  @Length(
      min = 5,
      max = 30,
      message = "Book author must be longer than 5 characters, shorter than 30 characters")
  private String author;

  private long readerId;

  public Book(String name, String author) {
    this.name = name;
    this.author = author;
  }

  public Book(long id, String name, String author) {
    this.id = id;
    this.name = name;
    this.author = author;
  }

  public Book(Long id, String name, String author, long readerId) {
    this.id = id;
    this.name = name;
    this.author = author;
    this.readerId = readerId;
  }

  public Book(String name, String author, long readerId) {
    this.name = name;
    this.author = author;
    this.readerId = readerId;
  }

  public Book() {}

  public void setReaderId(long readerId) {
    this.readerId = readerId;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAuthor() {
    return author;
  }

  public long getReaderId() {
    return readerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return Objects.equals(id, book.id)
        && readerId == book.readerId
        && Objects.equals(name, book.name)
        && Objects.equals(author, book.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, author, readerId);
  }

  @Override
  public String toString() {
    return "Book{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", author='"
        + author
        + '\''
        + ", readerId="
        + readerId
        + '}';
  }
}
