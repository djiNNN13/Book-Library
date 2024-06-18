package com.example.booklibrary.controllers;

import static java.util.stream.Collectors.toMap;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReaderController {
  private final List<Book> books = new ArrayList<>();
  private final List<Reader> readers = new ArrayList<>();
  private final AtomicLong bookCounter = new AtomicLong();
  private final AtomicLong readerCounter = new AtomicLong();
  private final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

  public ReaderController() {
    books.add(new Book(bookCounter.incrementAndGet(), "1984", "George Orwell", 1L));
    books.add(new Book(bookCounter.incrementAndGet(), "Home", "Tony Morrison", 1L));
    books.add(new Book(bookCounter.incrementAndGet(), "Glue", "Irvine Welsh"));

    readers.add(new Reader(readerCounter.incrementAndGet(), "Ivan"));
    readers.add(new Reader(readerCounter.incrementAndGet(), "Yevhenii"));
    readers.add(new Reader(readerCounter.incrementAndGet(), "Andrii"));
  }

  @GetMapping("/readers")
  public ResponseEntity<List<Reader>> getReaders() {
    return ResponseEntity.ok(readers);
  }

  @PostMapping("/readers")
  public ResponseEntity<Reader> saveReader(@RequestBody Reader reader) {
    readers.add(reader);
    return ResponseEntity.ok(reader);
  }

  @GetMapping("/readers/{readerId}/books")
  public ResponseEntity<List<Book>> getBorrowedBooksByReaderId(
      @PathVariable("readerId") @NotNull @Positive Long readerId) {
    var borrowedBooks = books.stream().filter(book -> readerId.equals(book.getReaderId())).toList();
    return ResponseEntity.ok(borrowedBooks);
  }

  @GetMapping("/readers/books")
  public ResponseEntity<Map<Reader, List<Book>>> getReadersWithBorrowedBooks() {
    Map<Reader, List<Book>> readersWithBooks =
        readers.stream()
            .collect(
                toMap(
                    reader -> reader,
                    reader ->
                        books.stream()
                            .filter(book -> reader.getId().equals(book.getReaderId()))
                            .toList()));
    return ResponseEntity.ok(readersWithBooks);
  }
}
