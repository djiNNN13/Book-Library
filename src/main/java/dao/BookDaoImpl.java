package dao;

import entity.Book;
import entity.Reader;
import exception.DaoOperationException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class BookDaoImpl implements BookDao {
  @Override
  public Book save(Book bookToSave) {
    var query = "INSERT INTO book(name, author) VALUES(?, ?)";
    try (var connection = DBUtil.getConnection();
        var insertStatement =
            connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
      Objects.requireNonNull(bookToSave, "Cannot save null value book");
      insertStatement.setString(1, bookToSave.getName());
      insertStatement.setString(2, bookToSave.getAuthor());
      insertStatement.executeUpdate();
      var generatedId = insertStatement.getGeneratedKeys();
      if (generatedId.next()) {
        bookToSave.setId(generatedId.getLong(1));
      }
      return bookToSave;
    } catch (SQLException e) {
      throw new DaoOperationException(String.format("Error saving book: %s", bookToSave), e);
    } catch (NullPointerException e) {
      throw new DaoOperationException(
          "Null pointer exception occurred while attempting to save the book. "
              + "Please ensure that the book object is not null.");
    }
  }

  @Override
  public void returnBook(long bookId) {
    var query = "UPDATE book SET reader_id = null WHERE id = ?";
    try (var connection = DBUtil.getConnection();
        var returnStatement = connection.prepareStatement(query)) {
      returnStatement.setLong(1, bookId);
      returnStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DaoOperationException(String.format("Error returning book with id: %d", bookId), e);
    }
  }

  @Override
  public Optional<Book> findById(long bookId) {
    var query = "SELECT id AS bookId, name AS bookName, author AS bookAuthor, reader_id FROM book WHERE id = ?";
    try (var connection = DBUtil.getConnection();
        var selectByIdStatement = connection.prepareStatement(query)) {
      selectByIdStatement.setLong(1, bookId);
      var resultSet = selectByIdStatement.executeQuery();
      if (resultSet.next()) {
        var book = DaoUtils.mapResultSetToBook(resultSet);
        return Optional.of(book);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding book with bookId: %d", bookId), e);
    }
  }

  @Override
  public List<Book> findAll() {
    var query = "SELECT id AS bookId, name AS bookName, author AS bookAuthor, reader_id FROM book";
    try (var connection = DBUtil.getConnection();
        var statement = connection.createStatement()) {
      var resultSet = statement.executeQuery(query);
      return DaoUtils.mapResultSetToBooksList(resultSet);
    } catch (SQLException e) {
      throw new DaoOperationException("Error finding all books", e);
    }
  }

  @Override
  public void borrow(long bookId, long readerId) {
    var query = "UPDATE book SET reader_id = ? WHERE id = ?";
    try (var connection = DBUtil.getConnection();
        var borrowStatement = connection.prepareStatement(query)) {
      borrowStatement.setLong(1, readerId);
      borrowStatement.setLong(2, bookId);
      borrowStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error borrowing book with id: %d for reader id: %d", bookId, readerId), e);
    }
  }

  @Override
  public List<Book> findAllByReaderId(long readerId) {
    var query =
        """
                SELECT id AS bookId,
                  name AS bookName,
                  author AS bookAuthor,
                  reader_id
                FROM book
                  WHERE reader_id = ?
                """;
    try (var connection = DBUtil.getConnection();
        var selectByReaderIdStatement = connection.prepareStatement(query)) {
      selectByReaderIdStatement.setLong(1, readerId);
      var resultSet = selectByReaderIdStatement.executeQuery();
      return DaoUtils.mapResultSetToBooksList(resultSet);
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding all books by reader id: %d", readerId), e);
    }
  }

  @Override
  public Map<Book, Optional<Reader>> findAllWithReaders() {
    var query =
        """
                SELECT
                  book.id AS bookId,
                  book.name AS bookName,
                  book.author AS bookAuthor,
                  book.reader_id,
                  reader.id AS readerId,
                  reader.name AS readerName
                FROM book
                  LEFT JOIN reader ON book.reader_id = reader.id
                     """;
    try (var connection = DBUtil.getConnection();
        var selectAllBooksWithReadersStatement = connection.createStatement()) {
      var resultSet = selectAllBooksWithReadersStatement.executeQuery(query);
      Map<Book, Optional<Reader>> map = new HashMap<>();
      while (resultSet.next()) {
        if (resultSet.getString("readerName") != null){
          var reader = DaoUtils.mapResultSetToReader(resultSet);
          map.put(DaoUtils.mapResultSetToBook(resultSet), Optional.of(reader));
        } else {
          map.put(DaoUtils.mapResultSetToBook(resultSet), Optional.empty());
        }
      }
      return map;
    } catch (SQLException e) {
      throw new DaoOperationException("Error finding books with their readers!");
    }
  }
}
