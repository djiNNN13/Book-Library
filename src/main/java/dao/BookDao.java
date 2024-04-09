package dao;

import entity.Book;
import entity.Reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookDao {
  Book save(Book bookToSave);

  void returnBook(long bookId);

  Optional<Book> findById(long id);

  List<Book> findAll();

  void borrow(long bookId, long readerId);

  List<Book> findAllByReaderId(long readerId);

  Map<Book, Optional<Reader>> findAllWithReaders();
}
