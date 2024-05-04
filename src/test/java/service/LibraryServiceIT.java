package service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import dao.BookDao;
import dao.BookDaoImpl;
import dao.ReaderDao;
import dao.ReaderDaoImpl;
import entity.Book;
import entity.Reader;
import integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

class LibraryServiceIT extends IntegrationTestBase {
  private final Validator validator = new Validator();
  private final BookDao bookDao = new BookDaoImpl();
  private final ReaderDao readerDao = new ReaderDaoImpl();
  private final LibraryService libraryService = new LibraryService(validator, bookDao, readerDao);

  @Test
  void borrowBook() {
    var book = bookDao.save(new Book("Test1", "TestAuthor"));
    var reader = readerDao.save(new Reader("TestReader"));
    bookDao.save(book);
    readerDao.save(reader);
    String bookId = String.valueOf(book.getId());
    String readerId = String.valueOf(reader.getId());
    validator.validateIdToBorrowBook(bookId + "/" + readerId);

    libraryService.borrowBook(bookId + "/" + readerId);

    assertThat(bookDao.findById(book.getId()).get().getReaderId()).isEqualTo(reader.getId());
  }
}
