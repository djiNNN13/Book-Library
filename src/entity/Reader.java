package entity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Reader {
  private static final AtomicLong count = new AtomicLong(0);
  private final long id;
  private final String name;

  public Reader(String name) {
    this.id = count.getAndIncrement();
    this.name = name;
  }

  public long getId() {
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
