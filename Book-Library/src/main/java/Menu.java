import exceptions.InvalidBookTitleException;
import exceptions.InvalidIdException;
import exceptions.InvalidInputFormatException;
import exceptions.InvalidNameException;

import java.util.Scanner;

public class Menu {
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
  private final Library library = new Library();
  private final Scanner scanner = new Scanner(System.in);
  private final LibraryService libraryService = new LibraryService(library);

  public void displayMenu() {
    boolean showOptions = true;
    System.out.println(SEPARATOR);
    System.out.println(WELCOME_MESSAGE + "\n\n" + OPTIONS_MESSAGE);
    boolean exitRequest = false;
    while (!exitRequest) {
      try {
        String option = scanner.nextLine().toLowerCase();
        switch (option) {
          case "1" -> libraryService.showAllBooks();
          case "2" -> libraryService.showAllReaders();
          case "3" -> {
            System.out.println("Please enter new reader full name!");
            libraryService.registerNewReader(getUserInput());
          }
          case "4" -> {
            System.out.println("Please enter new book name and author separated by “/”");
            libraryService.addNewBook(getUserInput());
          }
          case "5" -> {
            System.out.println(
                "Please enter book ID and reader ID separated by “/” to borrow a book.");
            libraryService.borrowBook(getUserInput());
          }
          case "6" -> {
            System.out.println("Please enter book ID to return a book.");
            libraryService.returnBookToLibrary(getUserInput());
          }
          case "7" -> {
            System.out.println("Please enter reader ID to show all his borrowed books");
            libraryService.showBorrowedBooks(getUserInput());
          }
          case "8" -> {
            System.out.println("Please enter book ID to show all his readers");
            libraryService.showCurrentReaderOfBook(getUserInput());
          }
          case "exit" -> {
            System.out.println("Goodbye!");
            exitRequest = true;
            showOptions = false;
          }
          default -> System.err.println(
              "Invalid option, please write correct option from the menu.");
        }
      } catch (InvalidNameException
               | InvalidIdException
               | InvalidInputFormatException
               | InvalidBookTitleException ex) {
        System.err.println(ex.getMessage());
      }
      if (showOptions) {
        System.out.println(SEPARATOR);
        System.out.println(OPTIONS_MESSAGE);
      }
    }
  }

  private String getUserInput() {
    return scanner.nextLine();
  }
}
