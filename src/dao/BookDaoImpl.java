package dao;

import entity.Book;
import exception.LibraryServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
  private final List<Book> books = new ArrayList<>();
  private static final String BOOK_NOT_FOUND = "This Book ID doesn't exist!";

  public BookDaoImpl() {
    save(new Book("1984", "George Orwell"));
    save(new Book("Home", "Tony Morrsion"));
    save(new Book("Glue", "Irvine Welsh"));
  }

  @Override
  public Book save(Book bookToSave) {
    books.add(bookToSave);
    return bookToSave;
  }

  @Override
  public void returnBookToLibrary(long bookId) {
    findById(bookId)
        .ifPresentOrElse(
            book -> {
              if (findReaderIdByBookId(bookId) == 0L) {
                throw new LibraryServiceException(
                    "Cannot return Book. Book is already in the Library!");
              }
              book.setReaderId(0);
            },
            () -> {
              throw new LibraryServiceException(BOOK_NOT_FOUND);
            });
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
    findById(bookId)
        .ifPresentOrElse(
            book -> {
              if (findReaderIdByBookId(bookId) != 0L) {
                throw new LibraryServiceException("Cannot borrow already borrowed Book!");
              }
              book.setReaderId(readerId);
            },
            () -> {
              throw new LibraryServiceException(BOOK_NOT_FOUND);
            });
  }

  @Override
  public List<Book> findAllByReaderId(long readerId) {
    return findAll().stream().filter(book -> book.getReaderId() == readerId).toList();
  }

  @Override
  public Long findReaderIdByBookId(long bookId) {
    return findById(bookId).get().getReaderId();
  }
}
