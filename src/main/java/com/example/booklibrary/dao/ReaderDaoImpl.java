package com.example.booklibrary.dao;

import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.DaoOperationException;
import java.util.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ReaderDaoImpl implements ReaderDao {
  private JdbcTemplate jdbcTemplate;

  public ReaderDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Reader save(Reader readerToSave) {
    var query = "INSERT INTO reader(name) VALUES(?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    try {
      Objects.requireNonNull(readerToSave);
      jdbcTemplate.update(
          con -> {
            var preparedStatement = con.prepareStatement(query, new String[] {"id"});
            preparedStatement.setString(1, readerToSave.getName());
            return preparedStatement;
          },
          keyHolder);
      if (keyHolder.getKey() != null) {
        readerToSave.setId(keyHolder.getKey().longValue());
      }
      return readerToSave;
    } catch (DataAccessException e) {
      throw new DaoOperationException(String.format("Error saving reader: %s", readerToSave), e);
    } catch (NullPointerException e) {
      throw new DaoOperationException(
          "Null pointer exception occurred while attempting to save the reader. "
              + "Please ensure that the reader object is not null.");
    }
  }

  @Override
  public Optional<Reader> findById(Long readerId) {
    var query = "SELECT id, name FROM reader WHERE id = ?";
    try {
      //noinspection DataFlowIssue
      return Optional.of(
          jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Reader.class), readerId));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    } catch (DataAccessException ex) {
      throw new DaoOperationException(
          String.format("Error finding reader with id: %d!", readerId), ex);
    }
  }

  @Override
  public List<Reader> findAll() {
    var query = "SELECT id, name FROM reader";
    try {
      return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Reader.class));
    } catch (DataAccessException e) {
      throw new DaoOperationException("Error finding all readers", e);
    }
  }

  @Override
  public Optional<Reader> findReaderByBookId(Long bookId) {
    var query =
        """
                SELECT
                  reader.id,
                  reader.name
                FROM reader
                  INNER JOIN book ON reader.id = book.reader_id WHERE book.id = ?
                """;
    try {
      //noinspection DataFlowIssue
      return Optional.of(
          jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Reader.class), bookId));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    } catch (DataAccessException ex) {
      throw new DaoOperationException(
          String.format("Error finding reader by book id: %d", bookId), ex);
    }
  }

  @Override
  public Map<Reader, List<Book>> findAllWithBooks() {
    var query =
        """
                SELECT
                  reader.id AS readerId,
                  reader.name AS readerName,
                  book.id AS bookId,
                  book.name AS bookName,
                  book.author AS bookAuthor,
                  book.reader_id
                FROM reader
                  LEFT JOIN book ON reader.id = book.reader_id
                """;
    try {
      return jdbcTemplate.query(query, DaoUtils.getReaderBooksExtractor());
    } catch (DataAccessException e) {
      throw new DaoOperationException("Error finding readers with borrowed books list!");
    }
  }
}
