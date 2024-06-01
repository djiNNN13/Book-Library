package com.example.booklibrary.dao;

import com.example.booklibrary.entity.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

  @Override
  public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    var book = new Book();

    book.setId(resultSet.getLong("bookId"));
    book.setName(resultSet.getString("bookName"));
    book.setAuthor(resultSet.getString("bookAuthor"));
    book.setReaderId(resultSet.getLong("reader_id"));

    return book;
  }
}
