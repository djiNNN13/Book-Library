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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryServiceIT extends IntegrationTestBase {
  private LibraryService libraryService;
  private Validator validator;
  private BookDao bookDao;
  private ReaderDao readerDao;

  @BeforeAll
  void init() {
    validator = new Validator();
    bookDao = new BookDaoImpl();
    readerDao = new ReaderDaoImpl();
    libraryService = new LibraryService(validator, bookDao, readerDao);
  }

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
