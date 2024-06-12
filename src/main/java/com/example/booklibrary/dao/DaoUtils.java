package com.example.booklibrary.dao;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.DaoOperationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DaoUtils implements ResultSetExtractor<Map<Book, Optional<Reader>>> {

  public static Reader mapResultSetToReader(ResultSet resultSet) {
    try {
      var reader = new Reader();
      reader.setId(resultSet.getLong("readerId"));
      reader.setName(resultSet.getString("readerName"));
      return reader;
    } catch (SQLException e) {
      throw new DaoOperationException("Cannot parse row to create reader instance", e);
    }
  }

  public static Book mapResultSetToBook(ResultSet resultSet) {
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

  public static List<Reader> mapResultSetToReadersList(ResultSet resultSet) throws SQLException {
    List<Reader> readers = new ArrayList<>();
    while (resultSet.next()) {
      var reader = mapResultSetToReader(resultSet);
      readers.add(reader);
    }
    return readers;
  }

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
