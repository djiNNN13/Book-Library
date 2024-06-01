package com.example.booklibrary.dao;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.DaoOperationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoUtils {
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
}
