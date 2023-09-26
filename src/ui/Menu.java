package ui;

import exception.InvalidBookTitleException;
import exception.InvalidIdException;
import exception.InvalidInputFormatException;
import exception.InvalidNameException;
import service.LibraryService;

import java.util.Scanner;

public class Menu {
  private static final String EXIT_MESSAGE = "Goodbye!";
  private static final String SET_GREEN_TEXT_COLOR = "\u001B[32m";
  private static final String SET_DEFAULT_TEXT_COLOR = "\u001B[0m";
  private static final String WELCOME_MESSAGE = "WELCOME TO THE LIBRARY!";
  private static final String OPTIONS_MESSAGE =
      """
            PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
                [1] SHOW ALL BOOKS IN THE LIBRARY
                [2] SHOW ALL READERS REGISTERED IN THE LIBRARY
                [3] REGISTER NEW READER
                [4] ADD NEW BOOK
                [5] BORROW A BOOK TO A READER
                [6] RETURN A BOOK TO THE LIBRARY
                [7] SHOW ALL BORROWED BOOK BY USER ID
                [8] SHOW CURRENT READER OF A BOOK WITH ID
            TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!
                    """;
  private static final String SEPARATOR =
      "-------------------------------------------------------------------";
  private final Scanner scanner = new Scanner(System.in);
  private final LibraryService libraryService = new LibraryService();

  public void displayMenu() {
    System.out.println(SEPARATOR);
    System.out.println(WELCOME_MESSAGE);
    while (true) {
      System.out.println(OPTIONS_MESSAGE);
      try {
        switch (scanner.nextLine().toLowerCase()) {
          case "1" -> showAllBooks();
          case "2" -> showAllReaders();
          case "3" -> addNewReader();
          case "4" -> addNewBook();
          case "5" -> borrowBook();
          case "6" -> returnBookToLibrary();
          case "7" -> showBorrowedBooks();
          case "8" -> showCurrentReaderOfBook();
          case "exit" -> exitFromMenu();
          default -> System.err.println(
              "Invalid option, please write correct option from the menu.");
        }
      } catch (InvalidNameException
          | InvalidIdException
          | InvalidInputFormatException
          | InvalidBookTitleException ex) {
        System.err.println(ex.getMessage());
      }
      System.out.println(SEPARATOR);
    }
  }

  private String getUserInput() {
    return scanner.nextLine();
  }

  private void showAllBooks() {
    System.out.print(SET_GREEN_TEXT_COLOR);
    libraryService.findAllBooks().forEach(System.out::println);
    System.out.print(SET_DEFAULT_TEXT_COLOR);
  }

  private void showAllReaders() {
    System.out.print(SET_GREEN_TEXT_COLOR);
    libraryService.findAllReader().forEach(System.out::println);
    System.out.print(SET_DEFAULT_TEXT_COLOR);
  }

  private void addNewReader() throws InvalidNameException {
    System.out.println("Please enter new reader full name!");
    String readerName = getUserInput();
    libraryService.addNewReader(readerName);
    System.out.print(SET_GREEN_TEXT_COLOR);
    System.out.println(readerName + " has successfully added!");
    System.out.print(SET_DEFAULT_TEXT_COLOR);
  }

  private void addNewBook()
      throws InvalidNameException, InvalidBookTitleException, InvalidInputFormatException {
    System.out.println("Please enter new book name and author separated by “/”");
    String book = getUserInput();
    libraryService.addNewBook(book);
    System.out.print(SET_GREEN_TEXT_COLOR);
    System.out.println(book + " has successfully added!");
    System.out.print(SET_DEFAULT_TEXT_COLOR);
  }

  private void borrowBook() throws InvalidInputFormatException, InvalidIdException {
    System.out.println("Please enter book ID and reader ID separated by “/” to borrow a book.");
    String bookIdAndReaderId = getUserInput();
    libraryService.borrowBook(bookIdAndReaderId);
    System.out.print(SET_GREEN_TEXT_COLOR);
    System.out.println(
        "Book has successfully borrowed by reader, with "
            + bookIdAndReaderId
            + " ID's respectively!");
    System.out.print(SET_DEFAULT_TEXT_COLOR);
  }

  private void returnBookToLibrary() throws InvalidIdException {
    System.out.println("Please enter book ID to return a book.");
    String bookToReturn = getUserInput();
    libraryService.returnBookToLibrary(bookToReturn);
    System.out.print(SET_GREEN_TEXT_COLOR);
    System.out.println(
        "Book with ID = " + bookToReturn + " has successfully returned to the library!");
    System.out.print(SET_GREEN_TEXT_COLOR);
  }

  private void showBorrowedBooks() throws InvalidIdException {
    System.out.println("Please enter reader ID to show all his borrowed books");
    String readerId = getUserInput();
    System.out.print(SET_GREEN_TEXT_COLOR);
    System.out.println(
        "Borrowed books by reader ID"
            + readerId
            + " : "
            + libraryService.showBorrowedBooks(readerId));
    System.out.print(SET_DEFAULT_TEXT_COLOR);
  }

  private void showCurrentReaderOfBook() throws InvalidIdException {
    System.out.println("Please enter book ID to show all his readers");
    String bookId = getUserInput();
    long readerId = libraryService.showCurrentReaderOfBook(bookId);
    System.out.print(SET_GREEN_TEXT_COLOR);
    System.out.println("Reader of Book ID " + bookId + " = " + readerId);
    System.out.print(SET_DEFAULT_TEXT_COLOR);
  }

  private void exitFromMenu() {
    scanner.close();
    System.out.println(SET_GREEN_TEXT_COLOR);
    System.out.println(EXIT_MESSAGE);
    System.exit(0);
  }
}
