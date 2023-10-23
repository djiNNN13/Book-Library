package dao;

import entity.Reader;
import exception.DaoOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReaderDaoImpl implements ReaderDao {
  private static final String SELECT_ALL_SQL = "SELECT * FROM reader;";
  private static final String INSERT_SQL = "INSERT INTO reader(name) VALUES(?);";
  private static final String SELECT_BY_ID_SQL = "SELECT * FROM reader WHERE id = ?;";
  private static final String SELECT_BY_BOOK_ID_SQL = "SELECT reader.id, reader.name FROM reader INNER JOIN book ON reader.id = book.reader_id WHERE book.id = ?;";

  @Override
  public Reader save(Reader readerToSave) {
      Objects.requireNonNull(readerToSave);
    try(var connection = DBUtil.getConnection()){
      saveReader(readerToSave, connection);
      return readerToSave;
    }catch (SQLException e){
      throw new DaoOperationException(String.format("Error saving reader: %s", readerToSave), e);
    }
  }

  private void saveReader(Reader readerToSave, Connection connection) throws SQLException {
    var updateStatement = prepareUpdateStatement(readerToSave, connection);
    updateStatement.executeUpdate();
    var id = fetchGeneratedId(updateStatement);
    readerToSave.setId(id);
  }
  private Long fetchGeneratedId(PreparedStatement updateStatement) throws SQLException {
    var generatedId = updateStatement.getGeneratedKeys();

    if(generatedId.next()){
      return generatedId.getLong(1);
    }else {
      throw new DaoOperationException("Cannot obtain reader ID");
    }
  }

  private PreparedStatement prepareUpdateStatement(Reader readerToSave, Connection connection) {
    try{
      var updateStatement = connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
      updateStatement.setString(1, readerToSave.getName());
      return updateStatement;
    }catch (SQLException e){
      throw new DaoOperationException(String.format("Cannot prepare update statement for reader: %s", readerToSave), e);
    }
  }

  @Override
  public Optional<Reader> findById(long readerId) {
    try(var connection = DBUtil.getConnection()){
      return findReaderById(readerId, connection);
    }catch (SQLException e){
      throw new DaoOperationException(String.format("Error finding reader with id: %d", readerId), e);
    }
  }

  private Optional<Reader> findReaderById(long readerId, Connection connection) throws SQLException {
    var selectByIdStatement = prepareSelectByIdStatement(readerId, connection);
    var resultSet = selectByIdStatement.executeQuery();

    if (resultSet.next()){
      var reader = parseRow(resultSet);
      return Optional.of(reader);
    }else {
      return Optional.empty();
    }
  }

  private Reader parseRow(ResultSet resultSet) {
    try{
      return createFromResultSet(resultSet);
    }catch (SQLException e){
      throw new DaoOperationException("Cannot parse row to create reader instance", e);
    }
  }

  private Reader createFromResultSet(ResultSet resultSet) throws SQLException {
    var reader = new Reader();

    reader.setId(resultSet.getLong("id"));
    reader.setName(resultSet.getString("name"));

    return reader;
  }

  private PreparedStatement prepareSelectByIdStatement(long readerId, Connection connection){
    try{
      var selectByIdStatement = connection.prepareStatement(SELECT_BY_ID_SQL);
      selectByIdStatement.setLong(1, readerId);
      return selectByIdStatement;
    }catch (SQLException e){
      throw new DaoOperationException(String.format("Cannot prepare select reader by id statement for id: %d", readerId), e);
    }

  }

  @Override
  public List<Reader> findAll() {
    try(var connection = DBUtil.getConnection()){
      return findAllReaders(connection);
    }catch (SQLException e){
      throw new DaoOperationException("Error finding all readers", e);
    }
  }

  private List<Reader> findAllReaders(Connection connection) throws SQLException {
    var selectAllStatement = connection.createStatement();
    var resultSet = selectAllStatement.executeQuery(SELECT_ALL_SQL);
    return collectToList(resultSet);
  }
  private List<Reader> collectToList(ResultSet resultSet) throws SQLException {
    List<Reader> readers = new ArrayList<>();
    while(resultSet.next()){
      var reader = parseRow(resultSet);
      readers.add(reader);
    }
    return readers;
  }

  @Override
  public Optional<Reader> findReaderByBookId(long bookId) {
    try(var connection = DBUtil.getConnection()){
      return findReaderByBook(bookId, connection);
    }catch (SQLException e){
      throw new DaoOperationException(String.format("Error finding reader by book id: %d", bookId), e);
    }
  }

  private Optional<Reader> findReaderByBook(long bookId, Connection connection) throws SQLException {
    var selectReaderByBookStatement = prepareSelectReaderByBookStatement(bookId, connection);
    var resultSet = selectReaderByBookStatement.executeQuery();

    if (resultSet.next()){
      var reader = parseRow(resultSet);
      return Optional.of(reader);
    }else {
      return Optional.empty();
    }
  }

  private  PreparedStatement prepareSelectReaderByBookStatement(long bookId, Connection connection) {
    try{
      var selectReaderByBookStatement = connection.prepareStatement(SELECT_BY_BOOK_ID_SQL);
      selectReaderByBookStatement.setLong(1, bookId);
      return selectReaderByBookStatement;
    } catch (SQLException e) {
      throw new DaoOperationException(String.format("Cannot prepare select reader by book id: %d", bookId), e);
    }
  }
}
