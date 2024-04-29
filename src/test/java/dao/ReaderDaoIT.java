package dao;

import entity.Book;
import entity.Reader;
import integration.IntegrationTestBase;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ReaderDaoIT extends IntegrationTestBase {
  private ReaderDao readerDao;
  private BookDao bookDao;

  @BeforeEach
  void init() {
    readerDao = new ReaderDaoImpl();
    bookDao = new BookDaoImpl();
  }

  @Test
  void save() {
    var readerToSave = getReader("Test1");

    var savedReader = readerDao.save(readerToSave);

    assertAll(
        () -> assertThat(savedReader.getId()).isNotNull(),
        () -> assertThat(savedReader.getName()).isEqualTo(readerToSave.getName()));
  }

  @Test
  void findById() {
    var reader = readerDao.save(getReader("Test1"));

    Optional<Reader> actualReader = readerDao.findById(reader.getId());

    assertThat(actualReader).isPresent();
    assertThat(actualReader.get()).isEqualTo(reader);
  }

  @Test
  void findAll() {
    var reader1 = readerDao.save(getReader("Test1"));
    var reader2 = readerDao.save(getReader("Test2"));
    var reader3 = readerDao.save(getReader("Test3"));

    List<Reader> actualReaders = readerDao.findAll();
    List<Long> readerIds = actualReaders.stream().map(Reader::getId).toList();

    assertThat(actualReaders).hasSize(3);
    assertThat(readerIds).contains(reader1.getId(), reader2.getId(), reader3.getId());
  }

  @Test
  void findAllWithBooks() {
    var book1 = bookDao.save(getBook("Test1", "Test1"));
    var book2 = bookDao.save(getBook("Test2", "Test2"));
    var reader1 = readerDao.save(getReader("Test1"));
    var reader2 = readerDao.save(getReader("Test2"));
    bookDao.borrow(book1.getId(), reader1.getId());
    bookDao.borrow(book2.getId(), reader2.getId());
    Map<Reader, List<Book>> expectedMap =
        Map.of(
            reader1, List.of(book1),
            reader2, List.of(book2));

    Map<Reader, List<Book>> actualMap = readerDao.findAllWithBooks();

    assertAll(
        () -> assertThat(actualMap).isEqualTo(expectedMap),
        () -> assertThat(actualMap).containsEntry(reader1, List.of(book1)),
        () -> assertThat(actualMap).containsEntry(reader2, List.of(book2)));
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
