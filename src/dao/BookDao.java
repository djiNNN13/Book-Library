package dao;

import entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
  Book save(Book book);

  void removeReader(Book book);

  Optional<Book> findById(long id);

  List<Book> findAll();

  void borrowBook(long bookId, long readerId);

  List<Book> findAllByReaderId(long readerId);

  Long findReaderIdByBookId(long bookId);
}
