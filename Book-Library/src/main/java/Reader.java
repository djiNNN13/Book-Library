import java.util.Objects;

public class Reader {
  private static int count = 0;
  private final int id;
  private final String name;

  public Reader(String name) {
    this.id = count;
    count++;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Reader reader = (Reader) o;
    return id == reader.id;
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
