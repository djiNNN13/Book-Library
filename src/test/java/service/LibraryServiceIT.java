package service;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import dao.BookDao;
import dao.BookDaoImpl;
import dao.ReaderDao;
import dao.ReaderDaoImpl;
import entity.Book;
import entity.Reader;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import integration.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryServiceIT extends IntegrationTestBase {
  private LibraryService libraryService;
  private Validator validator;
  private BookDao bookDao;
  private ReaderDao readerDao;

  @BeforeEach
  void init() {
    libraryService = new LibraryService(new Validator(), new BookDaoImpl(), new ReaderDaoImpl());
    validator = new Validator();
    bookDao = new BookDaoImpl();
    readerDao = new ReaderDaoImpl();
  }

  @Test
  void findAllBooks() {
    var book1 = bookDao.save(getBook("Test1", "Test1"));
    var book2 = bookDao.save(getBook("Test2", "Test2"));
    var book3 = bookDao.save(getBook("Test3", "Test3"));

    List<Book> actualBooks = libraryService.findAllBooks();
    List<Long> bookIds = actualBooks.stream().map(Book::getId).toList();

    assertThat(actualBooks).hasSize(3);
    assertThat(bookIds).contains(book1.getId(), book2.getId(), book3.getId());
  }

  @Test
  void findAllReader() {
    var reader1 = readerDao.save(getReader("Test1"));
    var reader2 = readerDao.save(getReader("Test2"));
    var reader3 = readerDao.save(getReader("Test3"));

    List<Reader> actualReaders = libraryService.findAllReader();
    List<Long> readerIds = actualReaders.stream().map(Reader::getId).toList();

    assertThat(actualReaders).hasSize(3);
    assertThat(readerIds).contains(reader1.getId(), reader2.getId(), reader3.getId());
  }

  @Test
  void showCurrentReaderOfBook() {
    var book = bookDao.save(getBook("Test1", "Test1"));
    var reader = readerDao.save(getReader("Test2"));
    var bookIdToCheck = String.valueOf(book.getId());
    validator.validateSingleId(bookIdToCheck);
    bookDao.borrow(book.getId(), reader.getId());

    Optional<Reader> actualReader = libraryService.showCurrentReaderOfBook(bookIdToCheck);

    assertThat(actualReader).isPresent();
    assertThat(actualReader.get()).isEqualTo(reader);
  }

  @Test
  void showBorrowedBooks() {
    var book1 = bookDao.save(getBook("Test1", "Test1"));
    var book2 = bookDao.save(getBook("Test2", "Test2"));
    var book3 = bookDao.save(getBook("Test3", "Test3"));
    var reader = readerDao.save(getReader("TestReader"));
    var readerIdToCheck = String.valueOf(reader.getId());
    validator.validateSingleId(readerIdToCheck);
    bookDao.borrow(book1.getId(), reader.getId());
    bookDao.borrow(book2.getId(), reader.getId());
    bookDao.borrow(book3.getId(), reader.getId());

    List<Book> actualBooks = libraryService.showBorrowedBooks(readerIdToCheck);
    List<Long> bookIds = actualBooks.stream().map(Book::getId).toList();

    assertThat(actualBooks).hasSize(3);
    assertThat(bookIds).contains(book1.getId(), book2.getId(), book3.getId());
  }

  @Test
  void addNewReader() {
    var reader = readerDao.save(getReader("TestReader"));
    var readerName = reader.getName();
    validator.validateName(readerName);

    libraryService.addNewReader(readerName);

    assertThat(readerDao.findById(reader.getId()).get()).isEqualTo(reader);
  }

  @Test
  void addNewBook() {
    var book = bookDao.save(getBook("Test1", "TestAuthor"));
    var bookName = book.getName();
    var bookAuthor = book.getAuthor();
    validator.validateNewBookInputFormat(bookName + "/" + bookAuthor);

    libraryService.addNewBook(bookName + "/" + bookAuthor);

    assertThat(bookDao.findById(book.getId()).get()).isEqualTo(book);
  }

  @Test
  void borrowBook() {
    var book = bookDao.save(getBook("Test1", "TestAuthor"));
    var reader = readerDao.save(getReader("TestReader"));
    bookDao.save(book);
    readerDao.save(reader);
    String bookId = String.valueOf(book.getId());
    String readerId = String.valueOf(reader.getId());
    validator.validateIdToBorrowBook(bookId + "/" + readerId);

    libraryService.borrowBook(bookId + "/" + readerId);

    assertThat(bookDao.findById(book.getId()).get().getReaderId()).isEqualTo(reader.getId());
  }

  @Test
  void returnBookToLibrary() {
    var book = bookDao.save(getBook("Test1", "TestAuthor"));
    var reader = readerDao.save(getReader("TestReader"));
    bookDao.borrow(book.getId(), reader.getId());
    String bookId = String.valueOf(book.getId());
    validator.validateSingleId(bookId);

    libraryService.returnBookToLibrary(bookId);

    assertThat(book.getReaderId()).isEqualTo(0);
  }

  @Test
  void findAllReadersWithBooks() {
    var book1 = bookDao.save(getBook("Test1", "TestAuthor"));
    var book2 = bookDao.save(getBook("Test2", "TestAuthor1"));
    var reader = readerDao.save(getReader("TestReader"));
    bookDao.borrow(book1.getId(), reader.getId());
    bookDao.borrow(book2.getId(), reader.getId());
    Map<Reader, List<Book>> expectedMap = Map.of(reader, List.of(book1, book2));

    Map<Reader, List<Book>> actualMap = libraryService.findAllReadersWithBooks();

    assertThat(actualMap).isEqualTo(expectedMap);
  }

  @Test
  void findAllBooksWithReaders() {
    var book1 = bookDao.save(getBook("Test1", "TestAuthor1"));
    var book2 = bookDao.save(getBook("Test2", "TestAuthor2"));
    var reader1 = readerDao.save(getReader("Test1"));
    var reader2 = readerDao.save(getReader("Test2"));
    bookDao.borrow(book1.getId(), reader1.getId());
    bookDao.borrow(book2.getId(), reader2.getId());
    Map<Book, Optional<Reader>> expectedMap =
        Map.of(
            book1, Optional.of(reader1),
            book2, Optional.of(reader2));

    Map<Book, Optional<Reader>> actualMap = libraryService.findAllBooksWithReaders();

    assertThat(actualMap).isEqualTo(expectedMap);
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
