package com.example.booklibrary.entity;

import java.util.Objects;

public class Reader {
  private long id;
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
