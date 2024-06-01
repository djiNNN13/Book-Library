package com.example.booklibrary.controllers;

import com.example.booklibrary.dao.BookDao;
import com.example.booklibrary.entity.Book;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BookController {
  private final BookDao bookDao;

  public BookController(BookDao bookDao) {
    this.bookDao = bookDao;
  }

  @GetMapping("/books")
  public List<Book> getBooks() {
    return bookDao.findAll();
  }

  @PostMapping("/books")
  public Book saveBook(@RequestBody Book book) {
    return bookDao.save(book);
  }
}
