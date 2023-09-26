package ui;

import exception.InvalidBookTitleException;
import exception.InvalidIdException;
import exception.InvalidInputFormatException;
import exception.InvalidNameException;
import service.LibraryService;

import java.util.Scanner;

public class Menu {
  private static final String EXIT_MESSAGE = "Goodbye!";
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
          case "7" -> showBorrowedBooksByReaderId();
          case "8" -> showCurrentReaderOfBookById();
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

  private void showAllBooks() {
    System.out.print("\u001B[32m");
    libraryService.findAllBooks().forEach(System.out::println);
    System.out.print("\u001B[0m");
  }

  private void showAllReaders() {
    System.out.print("\u001B[32m");
    libraryService.findAllReader().forEach(System.out::println);
    System.out.print("\u001B[0m");
  }

  private void addNewReader() throws InvalidNameException {
    System.out.println("Please enter new reader full name!");

    String readerName = scanner.nextLine();
    libraryService.addNewReader(readerName);

    System.out.print("\u001B[32m");
    System.out.println(readerName + " has successfully added!");
    System.out.print("\u001B[0m");
  }

  private void addNewBook()
      throws InvalidNameException, InvalidBookTitleException, InvalidInputFormatException {
    System.out.println("Please enter new book name and author separated by “/”");

    String book = scanner.nextLine();
    libraryService.addNewBook(book);

    System.out.print("\u001B[32m");
    System.out.println(book + " has successfully added!");
    System.out.print("\u001B[0m");
  }

  private void borrowBook() throws InvalidInputFormatException, InvalidIdException {
    System.out.println("Please enter book ID and reader ID separated by “/” to borrow a book.");

    String bookIdAndReaderId = scanner.nextLine();
    libraryService.borrowBook(bookIdAndReaderId);

    System.out.print("\u001B[32m");
    System.out.println(
        "Book has successfully borrowed by reader, with "
            + bookIdAndReaderId
            + " ID's respectively!");
    System.out.print("\u001B[0m");
  }

  private void returnBookToLibrary() throws InvalidIdException {
    System.out.println("Please enter book ID to return a book.");

    String bookToReturn = scanner.nextLine();
    libraryService.returnBookToLibrary(bookToReturn);

    System.out.print("\u001B[32m");
    System.out.println(
        "Book with ID = " + bookToReturn + " has successfully returned to the library!");
    System.out.print("\u001B[0m");
  }

  private void showBorrowedBooksByReaderId() throws InvalidIdException {
    System.out.println("Please enter reader ID to show all his borrowed books");
    String readerId = scanner.nextLine();
    System.out.print("\u001B[32m");
    System.out.println(
        "Borrowed books by reader ID"
            + readerId
            + " : "
            + libraryService.showBorrowedBooks(readerId));
    System.out.print("\u001B[0m");
  }

  private void showCurrentReaderOfBookById() throws InvalidIdException {
    System.out.println("Please enter book ID to show all his readers");

    String bookId = scanner.nextLine();
    long readerId = libraryService.showCurrentReaderOfBook(bookId);

    System.out.print("\u001B[32m");
    System.out.println("Reader of Book ID " + bookId + " = " + readerId);
    System.out.print("\u001B[0m");
  }

  private void exitFromMenu() {
    scanner.close();
    System.out.println("\u001B[32m");
    System.out.println(EXIT_MESSAGE);
    System.exit(0);
  }
}
