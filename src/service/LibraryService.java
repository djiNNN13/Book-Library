package service;

import dao.BookDao;
import dao.BookDaoImpl;
import dao.ReaderDao;
import dao.ReaderDaoImpl;
import entity.Book;
import entity.Reader;
import exception.InvalidBookTitleException;
import exception.InvalidIdException;
import exception.InvalidInputFormatException;
import exception.InvalidNameException;

import java.util.List;

public class LibraryService {
  private final Validator validator = new Validator();
  private final BookDao bookDao = new BookDaoImpl();
  private final ReaderDao readerDao = new ReaderDaoImpl();

  public List<Book> findAllBooks() {
    return bookDao.findAll();
  }

  public List<Reader> findAllReader() {
    return readerDao.findAll();
  }

  public Long showCurrentReaderOfBook(String userInput) throws InvalidIdException {
    validator.validateSingleId(userInput);

    int bookId = Integer.parseInt(userInput);
    long readerId = bookDao.findReaderIdByBookId(bookId);

    if (readerId == 0L) {
      throw new InvalidIdException("No readers found for this Book ID");
    }

    return readerId;
  }

  public List<Book> showBorrowedBooks(String userInput) throws InvalidIdException {
    validator.validateSingleId(userInput);

    long readerId = Long.parseLong(userInput);
    List<Book> borrowedBooks = bookDao.findAllByReaderId(readerId);

    if (borrowedBooks.isEmpty()) {
      throw new InvalidIdException("There is no borrowed books!");
    }

    return borrowedBooks;
  }

  public void addNewReader(String readerName) throws InvalidNameException {
    validator.validateName(readerName);
    readerDao.save(new Reader(readerName));
  }

  public void addNewBook(String book)
      throws InvalidBookTitleException, InvalidNameException, InvalidInputFormatException {
    if (!book.matches("^.*\\/.*$")) {
      throw new InvalidInputFormatException(
          "Invalid input format. Please use letters and exactly one '/' character.");
    }

    String[] parts = book.split("/");
    String bookTitle = parts[0];
    String authorName = parts[1];

    validator.validateBookTitle(bookTitle);
    validator.validateName(authorName);
    bookDao.save(new Book(bookTitle, authorName, 0));
  }

  public void borrowBook(String bookIdAndReaderId)
      throws InvalidInputFormatException, InvalidIdException {
    if (!bookIdAndReaderId.matches("^[^/]*[/][^/]*$")) {
      throw new InvalidInputFormatException(
          "Invalid input format. Please use exactly one '/' character.");
    }

    String[] ids = bookIdAndReaderId.split("/");
    validator.validateIdToBorrowBook(bookIdAndReaderId);

    long bookId = Long.parseLong(ids[0]);
    long readerId = Long.parseLong(ids[1]);
    Book bookToBorrow = bookDao.findById(bookId).orElseThrow(()-> new InvalidIdException("Book ID is not found!"));

    if (bookToBorrow.getReaderId() != 0){
      throw new InvalidIdException("Book is already borrowed!");
    }

    bookDao.borrowBook(bookId, readerId);
  }

  public void returnBookToLibrary(String bookIdToReturn) throws InvalidIdException {
    validator.validateSingleId(bookIdToReturn);

    int bookId = Integer.parseInt(bookIdToReturn);
    Book bookToReturn =
        bookDao.findById(bookId).orElseThrow(() -> new InvalidIdException("Book ID is not found!"));

    if (bookToReturn.getReaderId() == 0) {
      throw new InvalidIdException("Book doesn't have a reader!");
    }

    bookDao.returnBookToLibrary(bookId);
  }
}
