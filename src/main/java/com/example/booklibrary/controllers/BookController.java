package com.example.booklibrary.controllers;

import ch.qos.logback.classic.LoggerContext;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.BookNotFoundException;
import com.example.booklibrary.exception.ReaderNotFoundException;
import com.example.booklibrary.service.LibraryService;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/api/v1")
public class BookController {
  private final List<Book> books = new ArrayList<>();
  private final List<Reader> readers = new ArrayList<>();
  private final AtomicLong bookCounter = new AtomicLong();
  private final AtomicLong readerCounter = new AtomicLong();
  private final LibraryService libraryService;
  private final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

  public BookController(LibraryService libraryService) {
    this.libraryService = libraryService;

    books.add(new Book(bookCounter.incrementAndGet(), "1984", "George Orwell", 1L));
    books.add(new Book(bookCounter.incrementAndGet(), "Home", "Tony Morrison"));
    books.add(new Book(bookCounter.incrementAndGet(), "Glue", "Irvine Welsh"));

    readers.add(new Reader(readerCounter.incrementAndGet(), "Ivan"));
    readers.add(new Reader(readerCounter.incrementAndGet(), "Yevhenii"));
    readers.add(new Reader(readerCounter.incrementAndGet(), "Andrii"));
  }

  @GetMapping("/books")
  public ResponseEntity<List<Book>> getBooks() {
    var books = libraryService.findAllBooks();
    return ResponseEntity.ok(books);
  }

  @PostMapping("/books")
  public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
    var savedBook = libraryService.addNewBook(book);
    return ResponseEntity.ok(savedBook);
  }

  @PostMapping("/books/{bookId}/readers/{readerId}")
  public ResponseEntity<Book> borrowBookToReader(
      @PathVariable("bookId") @NotNull @Positive Long bookId,
      @PathVariable("readerId") @NotNull @Positive Long readerId) {
    var bookToBorrow =
        books.stream()
            .filter(book -> book.getId().equals(bookId))
            .findAny()
            .orElseThrow(() -> new BookNotFoundException("Book not found!"));

    var readerToBorrow =
        readers.stream()
            .filter(reader -> reader.getId().equals(readerId))
            .findAny()
            .orElseThrow(() -> new ReaderNotFoundException("Reader not found!"));

    bookToBorrow.setReaderId(readerToBorrow.getId());

    return ResponseEntity.ok(bookToBorrow);
  }

  @DeleteMapping("/books/{bookId}")
  public ResponseEntity<Book> returnBook(@PathVariable("bookId") @NotNull @Positive Long bookId) {
    var bookToReturn =
        books.stream()
            .filter(book -> book.getId().equals(bookId))
            .findAny()
            .orElseThrow(() -> new BookNotFoundException("Book not found!"));

    if ((bookToReturn.getReaderId() != null)) {
      bookToReturn.setReaderId(null);
    } else {
      return ResponseEntity.badRequest().body(bookToReturn);
    }

    return ResponseEntity.ok(bookToReturn);
  }

  @GetMapping("/books/{bookId}/reader")
  public ResponseEntity<Reader> getReaderByBookId(
      @PathVariable("bookId") @NotNull @Positive Long bookId) {
    var bookToFind =
        books.stream()
            .filter(book -> book.getId().equals(bookId))
            .findAny()
            .orElseThrow(() -> new BookNotFoundException("Book not found!"));

    if (bookToFind.getReaderId() == null) {
      return ResponseEntity.badRequest().body(null);
    }

    var readerToReturn =
        readers.stream()
            .filter(reader -> reader.getId().equals(bookToFind.getReaderId()))
            .findAny()
            .orElseThrow(() -> new ReaderNotFoundException("Reader not found!"));

    return ResponseEntity.ok(readerToReturn);
  }

  @GetMapping("/books/readers")
  public ResponseEntity<Map<Book, Optional<Reader>>> getBooksWithReaders() {
    Map<Book, Optional<Reader>> booksWithReaders =
        books.stream()
            .filter(book -> book.getReaderId() != null)
            .collect(
                toMap(
                    book -> book,
                    book ->
                        readers.stream()
                            .filter(reader -> reader.getId().equals(book.getReaderId()))
                            .findFirst()));

    return ResponseEntity.ok(booksWithReaders);
  }
}
