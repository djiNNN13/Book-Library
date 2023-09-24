package service;

import entity.Book;
import entity.Reader;
import exception.InvalidBookTitleException;
import exception.InvalidIdException;
import exception.InvalidInputFormatException;
import exception.InvalidNameException;
import dao.Library;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LibraryService {
  private final Validator validator = new Validator();
  private final Library library;
  private final Scanner scanner = new Scanner(System.in);

  public LibraryService(Library library) {
    this.library = library;
  }

  public void showAllBooks() {
    library.getBooks().forEach(System.out::println);
  }

  public void showAllReaders() {
    library.getReaders().forEach(System.out::println);
  }

  private Optional<Reader> findReaderOfBook(int bookId) {
    return library.getBorrowedBooks().entrySet().stream()
        .filter(entry -> entry.getKey().getId() == bookId)
        .map(Map.Entry::getValue)
        .findFirst();
  }

  private List<Book> findBorrowedBooksOfReader(int readerId) {
    return library.getBorrowedBooks().entrySet().stream()
        .filter(entry -> entry.getValue().getId() == readerId)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  public void showCurrentReaderOfBook() throws InvalidIdException {
    System.out.println("Please enter book ID to show all his readers");
    String userInput = getUserInput();
    validator.validateSingleId(userInput);
    int bookId = Integer.parseInt(userInput);
    Optional<Reader> reader = findReaderOfBook(bookId);
    if (reader.isPresent()) {
      System.out.println("Borrowed by reader ID: " + reader.get().getId());
    } else {
      System.err.println("No readers found for entity.Book ID = " + bookId);
    }
  }

  public void showBorrowedBooks() throws InvalidIdException {
    System.out.println("Please enter reader ID to show all his borrowed books");
    String userInput = getUserInput();
    validator.validateSingleId(userInput);
    int readerId = Integer.parseInt(userInput);
    List<Book> borrowedBooks = findBorrowedBooksOfReader(readerId);
    if (!borrowedBooks.isEmpty()) {
      for (Book book : borrowedBooks) {
        System.out.println(
            "Title: "
                + book.getName()
                + ", Author: "
                + book.getAuthor()
                + ", Borrowed by reader ID: "
                + readerId);
      }
    } else {
      System.err.println("No borrowed books found for entity.Reader ID = " + readerId);
    }
  }

  public void registerNewReader() throws InvalidNameException {
    System.out.println("Please enter new reader full name!");
    String readerName = getUserInput();
    validator.validateName(readerName);
    library.addReader(new Reader(readerName));
  }

  public void addNewBook() throws InvalidBookTitleException, InvalidNameException {
    System.out.println("Please enter new book name and author separated by “/”");
    String book = getUserInput();
    if (!book.matches("^.*\\/.*$")) {
      System.err.println("Invalid input format. Please use letters and exactly one '/' character.");
      return;
    }
    String[] parts = book.split("/");
    String bookTitle = parts[0];
    String authorName = parts[1];
    validator.validateBookTitle(bookTitle);
    validator.validateName(authorName);
    library.addBook(new Book(bookTitle, authorName));
  }

  public void borrowBook() throws InvalidInputFormatException, InvalidIdException {
    System.out.println(
            "Please enter book ID and reader ID separated by “/” to borrow a book.");
    String userInput = getUserInput();
    if (!userInput.matches("^[^/]*[/][^/]*$")) {
      System.err.println("Invalid input format. Please use exactly one '/' character.");
      return;
    }
    String[] ids = userInput.split("/");
    validator.validateIdToBorrowBook(userInput);
    int bookId = Integer.parseInt(ids[0]);
    int readerId = Integer.parseInt(ids[1]);

    Optional<Book> matchingBook =
        library.getBooks().stream().filter(book -> bookId == book.getId()).findFirst();
    if (matchingBook.isEmpty()) {
      System.err.println("Book with ID " + bookId + " not found!");
      return;
    }
    Book bookToBorrow = matchingBook.get();
    Optional<Reader> matchingReader =
        library.getReaders().stream().filter(reader -> readerId == reader.getId()).findFirst();
    if (matchingReader.isEmpty()) {
      System.err.println("Reader with ID " + readerId + " not found!");
      return;
    }
    Reader borrowingReader = matchingReader.get();

    library.removeBook(bookToBorrow);
    library.addBorrowedBook(bookToBorrow, borrowingReader);
    System.out.println(library.getBorrowedBooks());
  }

  public void returnBookToLibrary() throws InvalidIdException {
    System.out.println("Please enter book ID to return a book.");
    String userInput = getUserInput();
    validator.validateSingleId(userInput);
    int bookId = Integer.parseInt(userInput);
    Book bookToReturn =
        library.getBorrowedBooks().keySet().stream()
            .filter(book -> book.getId() == bookId)
            .findFirst()
            .orElse(null);
    if (bookToReturn == null) {
      System.err.println("Book with ID " + bookId + " not found!");
      return;
    }
    library.addBook(bookToReturn);
    library.removeBorrowedBook(bookToReturn);
    System.out.println(library.getBorrowedBooks());
  }

  private String getUserInput() {
    return scanner.nextLine();
  }
}
