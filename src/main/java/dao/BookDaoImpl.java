package dao;

import entity.Book;
import exception.DaoOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
  private static final String SELECT_ALL_SQL = "SELECT * FROM book;";
  private static final String INSERT_SQL = "INSERT INTO book(name, author) VALUES(?, ?);";
  private static final String SELECT_BY_ID_SQL = "SELECT * FROM book WHERE id = ?;";
  private static final String BORROW_BY_ID_SQL = "UPDATE book SET reader_id = ? WHERE id = ?;";
  private static final String RETURN_BY_ID_SQL = "UPDATE book SET reader_id = null WHERE id = ?;";
  private static final String SELECT_BOOK_BY_READER_ID_SQL =
      "SELECT * FROM book WHERE reader_id = ?;";

  @Override
  public Book save(Book bookToSave) {
    Objects.requireNonNull(bookToSave);
    try (var connection = DBUtil.getConnection()) {
      saveBook(bookToSave, connection);
      return bookToSave;
    } catch (SQLException e) {
      throw new DaoOperationException(String.format("Error saving book: %s", bookToSave), e);
    }
  }

  private void saveBook(Book bookToSave, Connection connection) throws SQLException {
    var insertStatement = prepareInsertStatement(bookToSave, connection);
    insertStatement.executeUpdate();
    var id = fetchGeneratedId(insertStatement);
    bookToSave.setId(id);
  }

  private Long fetchGeneratedId(PreparedStatement insertStatement) throws SQLException {
    var generatedKeys = insertStatement.getGeneratedKeys();

    if (generatedKeys.next()) {
      return generatedKeys.getLong(1);
    } else {
      throw new DaoOperationException("Cannot obtain book ID");
    }
  }

  private PreparedStatement prepareInsertStatement(Book bookToSave, Connection connection) {
    try {
      var insertStatement =
          connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
      fillBookStatement(bookToSave, insertStatement);
      return insertStatement;
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Cannot prepare insert statement for book: %s", bookToSave), e);
    }
  }

  private void fillBookStatement(Book bookToSave, PreparedStatement updateStatement)
      throws SQLException {
    updateStatement.setString(1, bookToSave.getName());
    updateStatement.setString(2, bookToSave.getAuthor());
  }

  @Override
  public void returnBook(long bookId) {
    try (var connection = DBUtil.getConnection()) {
      returnBookToLibrary(bookId, connection);
    } catch (SQLException e) {
      throw new DaoOperationException(String.format("Error returning book with id: %d", bookId), e);
    }
  }

  private void returnBookToLibrary(long bookId, Connection connection) throws SQLException {
    var returnStatement = prepareReturnStatement(bookId, connection);
    executeUpdateById(returnStatement, bookId);
  }

  private void executeUpdateById(PreparedStatement updateStatement, long bookId)
      throws SQLException {
    var affectedRows = updateStatement.executeUpdate();
    if (affectedRows == 0) {
      throw new DaoOperationException(String.format("Book with id = %d doesn't exists", bookId));
    }
  }

  private PreparedStatement prepareReturnStatement(long bookId, Connection connection) {
    try {
      var returnStatement = connection.prepareStatement(RETURN_BY_ID_SQL);
      returnStatement.setLong(1, bookId);
      return returnStatement;
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Cannot prepare return statement for book id: %d", bookId), e);
    }
  }

  @Override
  public Optional<Book> findById(long bookId) {
    try (var connection = DBUtil.getConnection()) {
      return findBookById(bookId, connection);
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding book with bookId: %d", bookId), e);
    }
  }

  private Optional<Book> findBookById(long id, Connection connection) throws SQLException {
    var selectByIdStatement = prepareSelectByIdStatement(id, connection);
    var resultSet = selectByIdStatement.executeQuery();
    if (resultSet.next()) {
      var book = parseRow(resultSet);
      return Optional.of(book);
    } else {
      return Optional.empty();
    }
  }

  private PreparedStatement prepareSelectByIdStatement(long bookId, Connection connection) {
    try {
      var selectByIdStatement = connection.prepareStatement(SELECT_BY_ID_SQL);
      selectByIdStatement.setLong(1, bookId);
      return selectByIdStatement;
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Cannot prepare select by id statement for book: %d", bookId), e);
    }
  }

  @Override
  public List<Book> findAll() {
    try (var connection = DBUtil.getConnection()) {
      return findAllBooks(connection);
    } catch (SQLException e) {
      throw new DaoOperationException("Error finding all books", e);
    }
  }

  private List<Book> findAllBooks(Connection connection) throws SQLException {
    var statement = connection.createStatement();
    var resultSet = statement.executeQuery(SELECT_ALL_SQL);
    return collectToList(resultSet);
  }

  private List<Book> collectToList(ResultSet resultSet) throws SQLException {
    List<Book> books = new ArrayList<>();
    while (resultSet.next()) {
      var book = parseRow(resultSet);
      books.add(book);
    }
    return books;
  }

  private Book parseRow(ResultSet resultSet) {
    try {
      return createFromResultSet(resultSet);
    } catch (SQLException e) {
      throw new DaoOperationException("Cannot parse row to create book instance", e);
    }
  }

  private Book createFromResultSet(ResultSet resultSet) throws SQLException {
    var book = new Book();
    book.setId(resultSet.getLong("id"));
    book.setName(resultSet.getString("name"));
    book.setAuthor(resultSet.getString("author"));
    book.setReaderId(resultSet.getLong("reader_id"));
    return book;
  }

  @Override
  public void borrow(long bookId, long readerId) {
    try (var connection = DBUtil.getConnection()) {
      borrowBook(bookId, readerId, connection);
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error borrowing book with id: %d for reader id: %d", bookId, readerId), e);
    }
  }

  private void borrowBook(long bookId, long readerId, Connection connection) throws SQLException {
    var borrowStatement = prepareBorrowStatement(bookId, readerId, connection);
    executeUpdateById(borrowStatement, bookId);
  }

  private PreparedStatement prepareBorrowStatement(
      long bookId, long readerId, Connection connection) {
    try {
      var borrowStatement = connection.prepareStatement(BORROW_BY_ID_SQL);
      fillBookStatement(bookId, readerId, borrowStatement);
      return borrowStatement;
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Cannot prepare statement for book id: %d", bookId), e);
    }
  }

  private void fillBookStatement(Long bookId, Long readerId, PreparedStatement updateStatement)
      throws SQLException {
    updateStatement.setLong(1, readerId);
    updateStatement.setLong(2, bookId);
  }

  @Override
  public List<Book> findAllByReaderId(long readerId) {
    try (var connection = DBUtil.getConnection()) {
      return findAllBooksByReaderId(readerId, connection);
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Error finding all books by reader id: %d", readerId), e);
    }
  }

  private List<Book> findAllBooksByReaderId(long readerId, Connection connection)
      throws SQLException {
    var selectByReaderIdStatement = prepareSelectByReaderIdStatement(readerId, connection);
    var resultSet = selectByReaderIdStatement.executeQuery();
    return collectToList(resultSet);
  }

  private PreparedStatement prepareSelectByReaderIdStatement(long readerId, Connection connection) {
    try {
      var selectByReaderIdStatement = connection.prepareStatement(SELECT_BOOK_BY_READER_ID_SQL);
      selectByReaderIdStatement.setLong(1, readerId);
      return selectByReaderIdStatement;
    } catch (SQLException e) {
      throw new DaoOperationException(
          String.format("Cannot prepare select all books by reader id: %d", readerId), e);
    }
  }
}
