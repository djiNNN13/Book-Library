package entity;

import java.util.Objects;

public class Book {
  private Long id;
  private String name;
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
    return id == book.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
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
