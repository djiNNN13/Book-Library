package com.example.booklibrary.service;

import static org.assertj.core.api.Assertions.*;

import com.example.booklibrary.dao.BookDao;
import com.example.booklibrary.dao.BookDaoImpl;
import com.example.booklibrary.dao.ReaderDao;
import com.example.booklibrary.dao.ReaderDaoImpl;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
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
