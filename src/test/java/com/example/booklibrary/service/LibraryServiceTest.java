package com.example.booklibrary.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.booklibrary.dao.BookDao;
import com.example.booklibrary.dao.ReaderDao;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.LibraryServiceException;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryServiceTest {
  private static final String BOOK_NOT_FOUND = "This Book ID doesn't exist!";
  private static final String READER_NOT_FOUND = "This Reader ID doesn't exist!";
  @Mock BookDao bookDao;
  @Mock ReaderDao readerDao;

  @InjectMocks LibraryService libraryService;

  @Test
  void findAllBooks() {
    List<Book> expectedBooks =
        List.of(new Book(1, "X", "X"), new Book(2, "Y", "Y"), new Book(3, "Z", "Z"));

    when(bookDao.findAll()).thenReturn(expectedBooks);

    List<Book> actualBooks = libraryService.findAllBooks();

    verify(bookDao).findAll();
    assertThat(actualBooks).isEqualTo(expectedBooks);
  }

  @Test
  void findAllBooksIfListIsEmpty() {
    List<Book> expectedBooks = new ArrayList<>();
    when(bookDao.findAll()).thenReturn(expectedBooks);

    List<Book> actualBooks = libraryService.findAllBooks();

    assertThat(actualBooks).isNotNull();
    assertThat(actualBooks).isEmpty();
  }

  @Test
  void findAllReader() {
    List<Reader> expectedReaders =
        List.of(new Reader(1, "X"), new Reader(2, "Y"), new Reader(3, "Z"));
    when(readerDao.findAll()).thenReturn(expectedReaders);

    List<Reader> actualReaders = libraryService.findAllReader();

    assertThat(actualReaders).isEqualTo(expectedReaders);
  }

  @Test
  void findAllReaderIfListIsEmpty() {
    List<Reader> expectedReaders = new ArrayList<>();
    when(readerDao.findAll()).thenReturn(expectedReaders);

    List<Reader> actualReaders = libraryService.findAllReader();

    assertThat(actualReaders).isNotNull();
    assertThat(actualReaders).isEmpty();
  }

  @Test
  void showCurrentReaderOfBook() {
    var bookIdToCheck = "1";
    var bookId = 1L;
    var expectedReader = new Reader(1, "Yevhenii");
    when(bookDao.findById(bookId)).thenReturn(Optional.of(new Book()));
    when(readerDao.findReaderByBookId(bookId)).thenReturn(Optional.of(expectedReader));

    Optional<Reader> actualReader = libraryService.showCurrentReaderOfBook(bookIdToCheck);

    assertThat(actualReader).isPresent();
    assertThat(actualReader).get().isEqualTo(expectedReader);
  }

  @Test
  void showCurrentReaderOfBookIfBookIsNotFound() {
    var bookIdToCheck = "99999";
    var bookId = 99999L;
    when(bookDao.findById(bookId)).thenReturn(Optional.empty());

    var exception =
        assertThrows(
            LibraryServiceException.class,
            () -> libraryService.showCurrentReaderOfBook(bookIdToCheck));

    assertThat(exception.getClass()).isEqualTo(LibraryServiceException.class);
    assertThat(exception.getMessage()).isEqualTo(BOOK_NOT_FOUND);
    verifyNoInteractions(readerDao);
  }

  @Test
  void showBorrowedBooks() {
    var readerIdToCheck = "1";
    var readerId = 1L;
    List<Book> expectedBooks =
        List.of(new Book(1, "X", "X"), new Book(2, "Y", "Y"), new Book(3, "Z", "Z"));
    when(readerDao.findById(readerId)).thenReturn(Optional.of(new Reader()));
    when(bookDao.findAllByReaderId(readerId)).thenReturn(expectedBooks);

    List<Book> actualBooks = libraryService.showBorrowedBooks(readerIdToCheck);

    assertThat(actualBooks).isEqualTo(expectedBooks);
  }

  @Test
  void showBorrowedBooksIfReaderIsNotFound() {
    var readerIdToCheck = "99999";
    var readerId = 99999L;
    when(readerDao.findById(readerId)).thenReturn(Optional.empty());

    var exception =
        assertThrows(
            LibraryServiceException.class, () -> libraryService.showBorrowedBooks(readerIdToCheck));

    assertThat(exception.getClass()).isEqualTo(LibraryServiceException.class);
    assertThat(exception.getMessage()).isEqualTo(READER_NOT_FOUND);
    verifyNoInteractions(bookDao);
  }

  @Test
  void addNewReader() {
    var readerName = "Yevhenii";

    libraryService.addNewReader(readerName);

    verify(readerDao).save(new Reader(readerName));
  }

  @Test
  void addNewBook() {
    var bookName = "Martin Eden/Jack London";
    String[] bookAndAuthor = {"Martin Eden", "Jack London"};

    libraryService.addNewBook(bookName);

    verify(bookDao).save(new Book(bookAndAuthor[0], bookAndAuthor[1]));
  }

  @Test
  void borrowBook() {
    var bookIdAndReaderId = "1/1";
    var bookId = 1L;
    var readerId = 1L;
    var book = new Book(bookId, "Martin Eden", "Jack London");
    var reader = new Reader(readerId, "Yevhenii");
    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
    when(readerDao.findById(readerId)).thenReturn(Optional.of(reader));
    when(readerDao.findReaderByBookId(bookId)).thenReturn(Optional.empty());

    libraryService.borrowBook(bookIdAndReaderId);
    verify(bookDao).borrow(bookId, readerId);
  }

  @Test
  void borrowBookIfBookIsNotFound() {
    var bookIdAndReaderId = "99999/1";
    var bookId = 99999L;
    var readerId = 1L;
    when(bookDao.findById(bookId)).thenReturn(Optional.empty());

    var exception =
        assertThrows(
            LibraryServiceException.class, () -> libraryService.borrowBook(bookIdAndReaderId));

    assertThat(exception.getClass()).isEqualTo(LibraryServiceException.class);
    assertThat(exception.getMessage()).isEqualTo(BOOK_NOT_FOUND);
    verifyNoInteractions(readerDao);
    verify(bookDao, times(0)).borrow(bookId, readerId);
  }

  @Test
  void borrowBookIfReaderIsNotFound() {
    var bookIdAndReaderId = "1/99999";
    var bookId = 1L;
    var readerId = 99999L;
    var book = new Book(bookId, "Martin Eden", "Jack London");
    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
    when(readerDao.findById(readerId)).thenReturn(Optional.empty());

    var exception =
        assertThrows(
            LibraryServiceException.class, () -> libraryService.borrowBook(bookIdAndReaderId));

    assertThat(exception.getClass()).isEqualTo(LibraryServiceException.class);
    assertThat(exception.getMessage()).isEqualTo(READER_NOT_FOUND);
    verify(bookDao, times(0)).borrow(bookId, readerId);
  }

  @Test
  void borrowBookIfBookIsBorrowed() {
    var bookIdAndReaderId = "1/1";
    var bookId = 1L;
    var readerId = 1L;
    var book = new Book(bookId, "Martin Eden", "Jack London", readerId);
    var reader = new Reader(readerId, "Yevhenii");
    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
    when(readerDao.findById(readerId)).thenReturn(Optional.of(reader));
    when(readerDao.findReaderByBookId(bookId)).thenReturn(Optional.of(reader));

    var exception =
        assertThrows(
            LibraryServiceException.class, () -> libraryService.borrowBook(bookIdAndReaderId));

    assertThat(exception.getClass()).isEqualTo(LibraryServiceException.class);
    assertThat(exception.getMessage()).isEqualTo("Cannot borrow already borrowed Book!");
    verify(bookDao, times(0)).borrow(bookId, readerId);
  }

  @Test
  void findAllReadersWithBooks() {
    Map<Reader, List<Book>> expectedMap =
        Map.of(
            new Reader(1, "X"),
                List.of(new Book(1, "dummy", "dummy"), new Book(2, "dummy1", "dummy1")),
            new Reader(2, "Y"), List.of(new Book(3, "dummy2", "dummy2")));
    when(readerDao.findAllWithBooks()).thenReturn(expectedMap);

    Map<Reader, List<Book>> actualMap = libraryService.findAllReadersWithBooks();

    assertThat(actualMap).isNotEmpty();
    assertThat(actualMap).isEqualTo(expectedMap);
  }

  @Test
  void findAllReadersWithBooksIfMapIsEmpty() {
    Map<Reader, List<Book>> expectedMap = new HashMap<>();
    when(readerDao.findAllWithBooks()).thenReturn(expectedMap);

    Map<Reader, List<Book>> actualMap = libraryService.findAllReadersWithBooks();

    assertThat(actualMap).isNotNull();
    assertThat(actualMap).isEmpty();
  }

  @Test
  void findAllBooksWithReaders() {
    Map<Book, Optional<Reader>> expectedMap =
        Map.of(
            new Book(1, "dummy1", "dummy2"),
            Optional.of(new Reader(1, "dummy")),
            new Book(2, "dummy3", "dummy4"),
            Optional.of(new Reader("dummy1")));
    when(bookDao.findAllWithReaders()).thenReturn(expectedMap);

    Map<Book, Optional<Reader>> actualMap = libraryService.findAllBooksWithReaders();

    assertThat(actualMap).isNotEmpty();
    assertThat(actualMap).isEqualTo(expectedMap);
  }

  @Test
  void findAllBooksWithReadersIfMapIsEmpty() {
    Map<Book, Optional<Reader>> expectedMap = new HashMap<>();
    when(bookDao.findAllWithReaders()).thenReturn(expectedMap);

    Map<Book, Optional<Reader>> actualMap = libraryService.findAllBooksWithReaders();

    assertThat(actualMap).isNotNull();
    assertThat(actualMap).isEmpty();
  }

  @Test
  void returnBookToLibrary() {
    var bookIdToReturn = "1";
    var bookId = 1L;
    var reader = new Reader(1L, "Yevhenii");
    var book = new Book(bookId, "Martin Eden", "Jack London", reader.getId());
    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
    when(readerDao.findReaderByBookId(bookId)).thenReturn(Optional.of(reader));

    libraryService.returnBookToLibrary(bookIdToReturn);
    verify(bookDao).returnBook(bookId);
  }

  @Test
  void returnBookToLibraryIfBookDoesNotExists() {
    var bookIdToReturn = "99999";
    var bookId = 99999L;
    when(bookDao.findById(bookId)).thenReturn(Optional.empty());

    var exception =
        assertThrows(
            LibraryServiceException.class,
            () -> libraryService.returnBookToLibrary(bookIdToReturn));

    assertThat(exception.getClass()).isEqualTo(LibraryServiceException.class);
    assertThat(exception.getMessage()).isEqualTo(BOOK_NOT_FOUND);
    verifyNoInteractions(readerDao);
    verify(bookDao, times(0)).returnBook(bookId);
  }

  @Test
  void returnBookToLibraryIfBookIsInLibrary() {
    var bookIdToReturn = "1";
    var bookId = 1L;
    var book = new Book(bookId, "Martin Eden", "Jack London");
    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
    when(readerDao.findReaderByBookId(bookId)).thenReturn(Optional.empty());

    var exception =
        assertThrows(
            LibraryServiceException.class,
            () -> libraryService.returnBookToLibrary(bookIdToReturn));

    assertThat(exception.getClass()).isEqualTo(LibraryServiceException.class);
    assertThat(exception.getMessage())
        .isEqualTo("Cannot return Book. Book is already in the Library!");
    verify(bookDao, times(0)).returnBook(bookId);
  }
}
