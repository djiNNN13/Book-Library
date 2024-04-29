package dao;

import entity.Book;
import entity.Reader;
import integration.IntegrationTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BookDaoIT extends IntegrationTestBase {
  private BookDao bookDao;
  private ReaderDao readerDao;

  @BeforeEach
  void init() {
    bookDao = new BookDaoImpl();
    readerDao = new ReaderDaoImpl();
  }

  @Test
  void save() {
    var bookToSave = getBook("Test1", "Test1");

    var savedBook = bookDao.save(bookToSave);

    assertAll(
        () -> assertThat(savedBook.getId()).isNotNull(),
        () -> assertThat(savedBook.getName()).isEqualTo(bookToSave.getName()),
        () -> assertThat(savedBook.getAuthor()).isEqualTo(bookToSave.getAuthor()));
  }

  @Test
  void findById() {
    var book = bookDao.save(getBook("Test1", "Test1"));

    Optional<Book> actualBook = bookDao.findById(book.getId());

    assertThat(actualBook).isPresent();
    assertThat(actualBook.get()).isEqualTo(book);
  }

  @Test
  void findAll() {
    var book1 = bookDao.save(getBook("Test1", "Test1"));
    var book2 = bookDao.save(getBook("Test2", "Test2"));
    var book3 = bookDao.save(getBook("Test3", "Test3"));

    List<Book> actualBooks = bookDao.findAll();
    List<Long> bookIds = actualBooks.stream().map(Book::getId).toList();

    assertThat(actualBooks).hasSize(3);
    assertThat(bookIds).contains(book1.getId(), book2.getId(), book3.getId());
  }

  @Test
  void findAllByReaderId() {
    var book1 = bookDao.save(getBook("Test1", "Test1"));
    var book2 = bookDao.save(getBook("Test2", "Test2"));
    var reader = readerDao.save(getReader("Test1"));
    bookDao.borrow(book1.getId(), reader.getId());
    bookDao.borrow(book2.getId(), reader.getId());
    List<Book> expectedBooks = List.of(book1, book2);

    List<Book> actualBooks = bookDao.findAllByReaderId(reader.getId());

    assertAll(
        () -> assertThat(actualBooks).hasSize(2),
        () -> assertThat(actualBooks).isEqualTo(expectedBooks),
        () -> assertThat(actualBooks.get(0).getReaderId()).isEqualTo(reader.getId()),
        () -> assertThat(actualBooks.get(1).getReaderId()).isEqualTo(reader.getId()));
  }

  @Test
  void findAllWithReaders() {
    var book1 = bookDao.save(getBook("Test1", "Test1"));
    var book2 = bookDao.save(getBook("Test2", "Test2"));
    var reader1 = readerDao.save(getReader("Test1"));
    var reader2 = readerDao.save(getReader("Test2"));
    bookDao.borrow(book1.getId(), reader1.getId());
    bookDao.borrow(book2.getId(), reader2.getId());
    Map<Book, Optional<Reader>> expectedMap =
        Map.of(
            book1, Optional.of(reader1),
            book2, Optional.of(reader2));

    Map<Book, Optional<Reader>> actualMap = bookDao.findAllWithReaders();

    assertAll(
        () -> assertThat(actualMap).isEqualTo(expectedMap),
        () -> assertThat(actualMap).containsEntry(book1, Optional.of(reader1)),
        () -> assertThat(actualMap).containsEntry(book2, Optional.of(reader2)));
  }

  private static Book getBook(String name, String author) {
    var book = new Book(name, author);
    return book;
  }

  private static Reader getReader(String name) {
    var reader = new Reader(name);
    return reader;
  }
}
