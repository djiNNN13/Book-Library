package com.example.booklibrary.controllers;

import com.example.booklibrary.dto.BookDTO;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.ReaderNotFoundException;
import com.example.booklibrary.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BookController {
  private final LibraryService libraryService;

  public BookController(LibraryService libraryService) {
    this.libraryService = libraryService;
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
  public ResponseEntity<Object> borrowBookToReader(
      @PathVariable("bookId") @NotNull @Positive Long bookId,
      @PathVariable("readerId") @NotNull @Positive Long readerId) {
    libraryService.borrowBook(bookId, readerId);
    return ResponseEntity.ok(
        String.format(
            "Book with id %d successfully borrowed to reader with id %d", bookId, readerId));
  }

  @DeleteMapping("/books/{bookId}")
  public ResponseEntity<Object> returnBook(@PathVariable("bookId") @NotNull @Positive Long bookId) {
    libraryService.returnBookToLibrary(bookId);
    return ResponseEntity.ok(
        String.format("Book with id %d successfully returned to the library", bookId));
  }

  @GetMapping("/books/{bookId}/reader")
  public ResponseEntity<Reader> getReaderByBookId(
      @PathVariable("bookId") @NotNull @Positive Long bookId) {
    var reader =
        libraryService
            .showCurrentReaderOfBook(bookId)
            .orElseThrow(
                () ->
                    new ReaderNotFoundException(
                        String.format(
                            "Cannot find reader by book = %d id! Book has not any reader",
                            bookId)));
    return ResponseEntity.ok(reader);
  }

  @GetMapping("/books/readers")
  public ResponseEntity<List<BookDTO>> getBooksWithReaders() {
    var booksWithReaders = libraryService.findAllBooksWithReaders();
    return ResponseEntity.ok(booksWithReaders);
  }
}
