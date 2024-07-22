package com.example.booklibrary.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql(scripts = "classpath:db/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ComponentScan
class ReaderDaoIT {
  @Autowired ReaderDao readerDao;
  @Autowired BookDao bookDao;
  @Autowired JdbcTemplate jdbcTemplate;

  @BeforeEach
  void cleanData() {
    jdbcTemplate.execute("DELETE FROM book");
    jdbcTemplate.execute("DELETE FROM reader");
  }

  @Test
  void saveAndFindById() {
    var readerToSave = generateReader("Test1");

    var savedReader = readerDao.save(readerToSave);
    Optional<Reader> actualReader = readerDao.findById(savedReader.getId());

    assertThat(actualReader).isPresent();
    assertAll(
        () -> assertThat(savedReader.getId()).isNotNull(),
        () -> assertThat(actualReader.get().getName()).isEqualTo(readerToSave.getName()));
  }

  @Test
  void findAll() {
    var reader1 = readerDao.save(generateReader("Test1"));
    var reader2 = readerDao.save(generateReader("Test2"));
    var reader3 = readerDao.save(generateReader("Test3"));

    List<Reader> actualReaders = readerDao.findAll();
    List<Long> readerIds = actualReaders.stream().map(Reader::getId).toList();

    assertThat(actualReaders).hasSize(3);
    assertThat(readerIds).contains(reader1.getId(), reader2.getId(), reader3.getId());

    assertAll(
        () -> assertThat(actualReaders.get(0).getName()).isEqualTo(reader1.getName()),
        () -> assertThat(actualReaders.get(1).getName()).isEqualTo(reader2.getName()),
        () -> assertThat(actualReaders.get(2).getName()).isEqualTo(reader3.getName()));
  }

  @Test
  void findAllWithBooks() {
    var book1 = bookDao.save(generateBook("Test1", "Test1"));
    var book2 = bookDao.save(generateBook("Test2", "Test2"));
    var book3 = bookDao.save(generateBook("Test3", "Test3"));

    var reader1 = readerDao.save(generateReader("Test1"));
    var reader2 = readerDao.save(generateReader("Test2"));
    var reader3 = readerDao.save(generateReader("Test3"));

    bookDao.borrow(book1.getId(), reader1.getId());
    bookDao.borrow(book2.getId(), reader1.getId());
    bookDao.borrow(book3.getId(), reader2.getId());

    book1.setReaderId(reader1.getId());
    book2.setReaderId(reader1.getId());
    book3.setReaderId(reader2.getId());

    Map<Reader, List<Book>> expectedMap = new HashMap<>();
    expectedMap.put(reader1, List.of(book1, book2));
    expectedMap.put(reader2, List.of(book1));
    expectedMap.put(reader3, Collections.emptyList());

    Map<Reader, List<Book>> actualMap = readerDao.findAllWithBooks();

    assertThat(actualMap)
        .hasSameSizeAs(expectedMap)
        .allSatisfy(
            (reader, books) ->
                assertThat(books).containsExactlyInAnyOrderElementsOf(expectedMap.get(reader)));
  }

  @Test
  void findReaderByBookId() {
    var book = bookDao.save(generateBook("Test1", "Test1"));
    var reader = readerDao.save(generateReader("Test2"));
    bookDao.borrow(book.getId(), reader.getId());
    book.setReaderId(reader.getId());

    Optional<Reader> actualReader = readerDao.findReaderByBookId(book.getId());

    assertThat(actualReader).isPresent();
    assertThat(actualReader.get()).isEqualTo(reader);
  }

  private static Book generateBook(String name, String author) {
    return new Book(name, author);
  }

  private static Reader generateReader(String name) {
    return new Reader(name);
  }
}
