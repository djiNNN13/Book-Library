package dao;

import entity.Book;
import entity.Reader;
import exception.DaoOperationException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ReaderDaoImpl implements ReaderDao {
  @Override
  public Reader save(Reader readerToSave) {
    var query = "INSERT INTO reader(name) VALUES(?)";
    try (var connection = DBUtil.getConnection();
        var updateStatement =
            connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
      Objects.requireNonNull(readerToSave, "Cannot save null value reader");
      updateStatement.setString(1, readerToSave.getName());
      updateStatement.executeUpdate();
      var generatedId = updateStatement.getGeneratedKeys();
      if (generatedId.next()) {
        readerToSave.setId(generatedId.getLong(1));
      }
      return readerToSave;
    } catch (SQLException e) {
      throw new DaoOperationException(String.format("Error saving reader: %s", readerToSave), e);
    } catch (NullPointerException e) {
      throw new DaoOperationException(
          "Null pointer exception occurred while attempting to save the reader. "
              + "Please ensure that the reader object is not null.");
    }
  }

  @Override
  public Optional<Reader> findById(long readerId) {
    var query = "SELECT id AS readerId, name AS readerName FROM reader WHERE id = ?";
    try (var connection = DBUtil.getConnection();
        var selectByIdStatement = connection.prepareStatement(query)) {
      selectByIdStatement.setLong(1, readerId);
      var resultSet = selectByIdStatement.executeQuery();
      if (resultSet.next()) {
        var reader = DaoUtils.mapResultSetToReader(resultSet);
        return Optional.of(reader);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding reader with id: %d", readerId), e);
    }
  }

  @Override
  public List<Reader> findAll() {
    var query = "SELECT id AS readerId, name AS readerName FROM reader";
    try (var connection = DBUtil.getConnection();
        var selectAllStatement = connection.createStatement()) {
      var resultSet = selectAllStatement.executeQuery(query);
      return DaoUtils.mapResultSetToReadersList(resultSet);
    } catch (SQLException e) {
      throw new DaoOperationException("Error finding all readers", e);
    }
  }

  @Override
  public Optional<Reader> findReaderByBookId(long bookId) {
    var query =
        """
                SELECT
                  reader.id AS readerId,
                  reader.name AS readerName
                FROM reader
                  INNER JOIN book ON reader.id = book.reader_id WHERE book.id = ?
                """;
    try (var connection = DBUtil.getConnection();
        var selectReaderByBookStatement = connection.prepareStatement(query)) {
      selectReaderByBookStatement.setLong(1, bookId);
      var resultSet = selectReaderByBookStatement.executeQuery();
      if (resultSet.next()) {
        var reader = DaoUtils.mapResultSetToReader(resultSet);
        return Optional.of(reader);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding reader by book id: %d", bookId), e);
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
    try (var connection = DBUtil.getConnection();
        var selectAllReadersWithBooksStatement = connection.createStatement()) {
      var resultSet = selectAllReadersWithBooksStatement.executeQuery(query);
      Map<Reader, List<Book>> map = new HashMap<>();
      while (resultSet.next()) {
        var borrowedBooks =  map.computeIfAbsent(DaoUtils.mapResultSetToReader(resultSet), k -> new ArrayList<>());
        if (resultSet.getString("bookName") != null) {
          var book = DaoUtils.mapResultSetToBook(resultSet);
          borrowedBooks.add(book);
        }
      }
      return map;
    } catch (SQLException e) {
      throw new DaoOperationException("Error finding readers with borrowed books list!");
    }
  }
}
