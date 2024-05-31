package com.example.booklibrary.controllers;

import com.example.booklibrary.entity.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1")
public class BookController {
  private final List<Book> books = new ArrayList<>();
  private final AtomicLong counter = new AtomicLong();

  public BookController() {
    books.add(new Book(counter.incrementAndGet(), "1984", "George Orwell"));
    books.add(new Book(counter.incrementAndGet(), "Home", "Tony Morrison"));
    books.add(new Book(counter.incrementAndGet(), "Glue", "Irvine Welsh"));
  }

  @GetMapping("/books")
  public List<Book> getBooks() {
    return books;
  }

  @PostMapping("/books")
  public Book saveBook(@RequestBody Book book) {
    book.setId(counter.incrementAndGet());
    books.add(book);
    return book;
  }
}
