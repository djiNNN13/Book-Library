package entity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Book {
  private static final AtomicLong count = new AtomicLong(0);
  private final long id;
  private final String name;
  private final String author;
  private long readerId;

  public Book(String name, String author, long readerId) {
    this.id = count.incrementAndGet();
    this.name = name;
    this.author = author;
    this.readerId = readerId;
  }

  public long getId() {
    return id;
  }

  public long getReaderId() {
    return readerId;
  }

  public void setReaderId(long readerId) {
    this.readerId = readerId;
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
    return "Book{" + "id=" + id + ", Name='" + name + '\'' + ", Author='" + author + '\'' + '}';
  }
}
