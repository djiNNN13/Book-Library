package dao;

import entity.Reader;
import exception.DaoOperationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReaderDaoImpl implements ReaderDao {
  @Override
  public Reader save(Reader readerToSave) {
    String INSERT_SQL = "INSERT INTO reader(name) VALUES(?)";
    try (var connection = DBUtil.getConnection();
        var updateStatement =
            connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
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
      throw new DaoOperationException(e.getMessage());
    }
  }

  @Override
  public Optional<Reader> findById(long readerId) {
    String SELECT_BY_ID_SQL = "SELECT id, name FROM reader WHERE id = ?";
    try (var connection = DBUtil.getConnection();
        var selectByIdStatement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
      selectByIdStatement.setLong(1, readerId);
      var resultSet = selectByIdStatement.executeQuery();
      if (resultSet.next()) {
        var reader = mapResultSetToReader(resultSet);
        return Optional.of(reader);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding reader with id: %d", readerId), e);
    }
  }

  private Reader mapResultSetToReader(ResultSet resultSet) {
    try {
      var reader = new Reader();
      reader.setId(resultSet.getLong("id"));
      reader.setName(resultSet.getString("name"));
      return reader;
    } catch (SQLException e) {
      throw new DaoOperationException("Cannot parse row to create reader instance", e);
    }
  }

  @Override
  public List<Reader> findAll() {
    String SELECT_ALL_SQL = "SELECT id, name FROM reader";
    try (var connection = DBUtil.getConnection();
        var selectAllStatement = connection.createStatement()) {
      var resultSet = selectAllStatement.executeQuery(SELECT_ALL_SQL);
      return collectToList(resultSet);
    } catch (SQLException e) {
      throw new DaoOperationException("Error finding all readers", e);
    }
  }

  private List<Reader> collectToList(ResultSet resultSet) throws SQLException {
    List<Reader> readers = new ArrayList<>();
    while (resultSet.next()) {
      var reader = mapResultSetToReader(resultSet);
      readers.add(reader);
    }
    return readers;
  }

  @Override
  public Optional<Reader> findReaderByBookId(long bookId) {
    String SELECT_BY_BOOK_ID_SQL =
        "SELECT reader.id, reader.name FROM reader INNER JOIN book ON reader.id = book.reader_id WHERE book.id = ?";
    try (var connection = DBUtil.getConnection();
        var selectReaderByBookStatement = connection.prepareStatement(SELECT_BY_BOOK_ID_SQL)) {
      selectReaderByBookStatement.setLong(1, bookId);
      var resultSet = selectReaderByBookStatement.executeQuery();
      if (resultSet.next()) {
        var reader = mapResultSetToReader(resultSet);
        return Optional.of(reader);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding reader by book id: %d", bookId), e);
    }
  }
}
