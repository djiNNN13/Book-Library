package com.example.booklibrary.controllers;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.service.LibraryService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.booklibrary.util.ErrorDetail;
import com.example.booklibrary.util.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleInvalidArguments(MethodArgumentNotValidException ex) {
    List<ErrorDetail> errors = new ArrayList<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(new ErrorDetail(error.getField(), error.getDefaultMessage()));
    }
    ErrorResponse errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
            "Failed to create a new book, the request contains invalid fields",
            errors);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
