package dao;

import entity.Book;
import entity.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookDaoImpl implements BookDao {
  private final List<Book> books = new ArrayList<>();

  public BookDaoImpl() {
    save(new Book("1984", "George Orwell", 0));
    save(new Book("Home", "Tony Morrsion", 0));
    save(new Book("Glue", "Irvine Welsh", 0));
  }

  @Override
  public Book save(Book book) {
    books.add(book);
    return book;
  }

  @Override
  public void removeReader(Book book) {
    book.setReaderId(0);
  }

  @Override
  public Optional<Book> findById(long id) {
    return books.stream().filter(book -> book.getId() == id).findFirst();
  }

  @Override
  public List<Book> findAll() {
    return books;
  }

  @Override
  public void borrowBook(long bookId, long readerId) {
    findById(bookId).get().setReaderId(readerId);
  }

  @Override
  public List<Book> findAllByReaderId(long readerId) {
    return findAll().stream()
        .filter(book -> book.getReaderId() == readerId)
        .collect(Collectors.toList());
  }

  @Override
  public Long findReaderIdByBookId(long bookId) {
    return findById(bookId).get().getReaderId();
  }
}
