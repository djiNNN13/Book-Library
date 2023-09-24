package dao;

import entity.Book;
import entity.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
  private final List<Book> books;
  private final List<Reader> readers;
  private final Map<Book, Reader> borrowedBooks = new HashMap<>();

  public Library() {
    books = new ArrayList<>();
    readers = new ArrayList<>();

    books.add(new Book("1984", "George Orwell"));
    books.add(new Book("Home", "Tony Morrsion"));
    books.add(new Book("Glue", "Irvine Welsh"));

    readers.add(new Reader("Ivan"));
    readers.add(new Reader("Yevhenii"));
    readers.add(new Reader("Andrii"));
  }

  public List<Book> getBooks() {
    return books;
  }

  public List<Reader> getReaders() {
    return readers;
  }

  public Map<Book, Reader> getBorrowedBooks() {
    return borrowedBooks;
  }

  public void removeBook(Book book) {
    books.remove(book);
  }

  public void removeBorrowedBook(Book book) {
    borrowedBooks.remove(book);
  }

  public void addBorrowedBook(Book book, Reader reader) {
    borrowedBooks.put(book, reader);
  }

  public void addBook(Book book) {
    books.add(book);
  }

  public void addReader(Reader reader) {
    readers.add(reader);
  }
}
