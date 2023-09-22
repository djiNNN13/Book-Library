import java.util.Objects;

public class Book {
  private static int count = 0;
  private final int id;
  private final String name;
  private final String author;

  public Book(String name, String author) {
    this.id = count;
    count++;
    this.name = name;
    this.author = author;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAuthor() {
    return author;
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
