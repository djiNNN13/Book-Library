package com.example.booklibrary.dao;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.DaoOperationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DaoUtils {
  public static ResultSetExtractor<Map<Book, Reader>> getBookReaderExtractor() {
    return rs -> {
      Map<Book, Reader> map = new HashMap<>();

      while (rs.next()) {
        var book = mapResultSetToBook(rs);
        var reader = mapResultSetToReader(rs);
        map.put(book, reader);
      }
      return map;
    };
  }

  public static ResultSetExtractor<Map<Reader, List<BookDto>>> getReaderBooksExtractor() {
    return rs -> {
      Map<Reader, List<BookDto>> map = new HashMap<>();

      while (rs.next()) {
        var reader = mapResultSetToReader(rs);
        List<BookDto> borrowedBooks = map.computeIfAbsent(reader, k -> new ArrayList<>());
        BookDto book = DaoUtils.mapResultSetToBookDto(rs);
        borrowedBooks.add(book);
      }
      return map;
    };
  }

  private static Reader mapResultSetToReader(ResultSet resultSet) {
    try {
      var reader = new Reader();
      reader.setId(resultSet.getLong("readerId"));
      reader.setName(resultSet.getString("readerName"));
      return reader;
    } catch (SQLException e) {
      throw new DaoOperationException("Cannot parse row to create reader instance", e);
    }
  }

  private static Book mapResultSetToBook(ResultSet resultSet) {
    try {
      var book = new Book();
      book.setId(resultSet.getLong("bookId"));
      book.setName(resultSet.getString("bookName"));
      book.setAuthor(resultSet.getString("bookAuthor"));
      book.setReaderId(resultSet.getObject("readerId", Long.class));
      return book;
    } catch (SQLException e) {
      throw new DaoOperationException("Cannot parse row to create book instance", e);
    }
  }

  private static BookDto mapResultSetToBookDto(ResultSet resultSet) {
    try {
      var bookDto = new BookDto();
      bookDto.setId(resultSet.getLong("bookId"));
      bookDto.setName(resultSet.getString("bookName"));
      bookDto.setAuthor(resultSet.getString("bookAuthor"));
      return bookDto;
    } catch (SQLException e) {
      throw new DaoOperationException("Cannot parse row to create bookDto instance", e);
    }
  }
}
