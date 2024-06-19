package com.example.booklibrary.dao;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.DaoOperationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DaoUtils {
  private static class BookReaderExtractor
      implements ResultSetExtractor<Map<Book, Optional<Reader>>> {
    @Override
    public Map<Book, Optional<Reader>> extractData(ResultSet rs)
        throws SQLException, DataAccessException {
      Map<Book, Optional<Reader>> map = new HashMap<>();

      while (rs.next()) {
        var book = mapResultSetToBook(rs);
        var reader = rs.getString("readerName") != null ? mapResultSetToReader(rs) : null;
        map.put(book, Optional.ofNullable(reader));
      }
      return map;
    }
  }
  public static ResultSetExtractor<Map<Book, Optional<Reader>>> getBookReaderExtractor(){
    return new BookReaderExtractor();
  }

  private static class ReaderBooksExtractor implements ResultSetExtractor<Map<Reader, List<Book>>> {
    @Override
    public Map<Reader, List<Book>> extractData(ResultSet rs) throws SQLException {
      Map<Reader, List<Book>> map = new HashMap<>();

      while (rs.next()) {
        var reader = mapResultSetToReader(rs);
        List<Book> borrowedBooks = map.computeIfAbsent(reader, k -> new ArrayList<>());
        if (rs.getString("bookName") != null) {
          Book book = DaoUtils.mapResultSetToBook(rs);
          borrowedBooks.add(book);
        }
      }
      return map;
    }
  }

  public static ResultSetExtractor<Map<Reader, List<Book>>> getReaderBooksExtractor() {
    return new ReaderBooksExtractor();
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
      book.setReaderId(resultSet.getLong("reader_id"));
      return book;
    } catch (SQLException e) {
      throw new DaoOperationException("Cannot parse row to create book instance", e);
    }
  }
}
