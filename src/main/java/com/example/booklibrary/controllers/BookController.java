package com.example.booklibrary.controllers;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.service.LibraryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BookController {
  private final LibraryService libraryService;

  public BookController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @GetMapping("/books")
  public List<Book> getBooks() {
    return libraryService.findAllBooks();
  }

  @PostMapping("/books")
  public Book saveBook(@Valid @RequestBody Book book) {
    return libraryService.addNewBook(book);
  }
}
