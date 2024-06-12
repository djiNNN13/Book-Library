package com.example.booklibrary.controllers;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.service.LibraryService;
import jakarta.validation.Valid;
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
}
