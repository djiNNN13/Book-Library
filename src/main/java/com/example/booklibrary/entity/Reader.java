package com.example.booklibrary.entity;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public class Reader {
  @Positive private long id;

  @NotBlank(message = "Book author cannot be null!")
  @Pattern(
      regexp = "^[a-zA-Z\\s'-]+$",
      message = "Book author must contain only ENGLISH letters, spaces, dashes, apostrophes")
  @Length(
      min = 5,
      max = 30,
      message = "Book author must be longer than 5 characters, shorter than 30 characters")
  private String name;

  public Reader(String name) {
    this.name = name;
  }

  public Reader(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Reader() {}

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Reader reader = (Reader) o;
    return id == reader.id && Objects.equals(name, reader.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Reader{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
