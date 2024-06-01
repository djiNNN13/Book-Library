package com.example.booklibrary.dao;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BookReaderExtractor implements ResultSetExtractor<Map<Book, Optional<Reader>>> {

  @Override
  public Map<Book, Optional<Reader>> extractData(ResultSet rs)
      throws SQLException, DataAccessException {
    Map<Book, Optional<Reader>> map = new HashMap<>();

    while (rs.next()) {
      var book = DaoUtils.mapResultSetToBook(rs);
      var reader = rs.getString("readerName") != null ? DaoUtils.mapResultSetToReader(rs) : null;
      map.put(book, Optional.ofNullable(reader));
    }
    return map;
  }
}
