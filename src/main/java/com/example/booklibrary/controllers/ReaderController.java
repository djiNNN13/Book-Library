package com.example.booklibrary.controllers;

import com.example.booklibrary.dto.ReaderWithBooksDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.SaveReaderException;
import com.example.booklibrary.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1")
public class ReaderController {
  private final LibraryService libraryService;

  public ReaderController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @GetMapping("/readers")
  public ResponseEntity<List<Reader>> getReaders() {
    var readers = libraryService.findAllReader();
    return ResponseEntity.ok(readers);
  }

  @PostMapping("/readers")
  public ResponseEntity<Reader> saveReader(@Valid @RequestBody Reader reader) {
    if (reader.getId() != null) {
      throw new SaveReaderException(
          "Request body should not contain reader id value");
    }
    var savedReader = libraryService.addNewReader(reader);
    return ResponseEntity.ok(savedReader);
  }

  @GetMapping("/readers/{readerId}/books")
  public ResponseEntity<List<Book>> getBorrowedBooksByReaderId(
      @PathVariable("readerId") @NotNull @Positive(message = "Reader ID must be a positive number") Long readerId) {
    var books = libraryService.showBorrowedBooks(readerId);
    return ResponseEntity.ok(books);
  }

  @GetMapping("/readers/books")
  public ResponseEntity<List<ReaderWithBooksDto>> getReadersWithBorrowedBooks() {
    var readersWithBooks = libraryService.findAllReadersWithBooks();
    return ResponseEntity.ok(readersWithBooks);
  }
}
