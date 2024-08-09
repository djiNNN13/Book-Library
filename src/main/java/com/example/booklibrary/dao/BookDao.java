package com.example.booklibrary.dao;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
public interface BookDao {
  Book save(Book bookToSave);

  void returnBook(long bookId);

  Optional<Book> findById(long id);

  List<BookDto> findAll();

  void borrow(long bookId, long readerId);

  List<BookDto> findAllByReaderId(long readerId);

  Map<Book, Reader> findAllWithReaders();
}
