package views;

import exceptions.InvalidBookTitleException;
import exceptions.InvalidIdException;
import exceptions.InvalidInputFormatException;
import exceptions.InvalidNameException;
import services.LibraryService;
import storage.Library;

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
  private boolean showOptions = true;
  private boolean exitRequest = false;

  public void displayMenu() {
    System.out.println(SEPARATOR);
    System.out.println(WELCOME_MESSAGE + "\n\n" + OPTIONS_MESSAGE);
    while (!exitRequest) {
      try {
        String option = scanner.nextLine().toLowerCase();
        switch (option) {
          case "1" -> libraryService.showAllBooks();
          case "2" -> libraryService.showAllReaders();
          case "3" -> libraryService.registerNewReader();
          case "4" -> libraryService.addNewBook();
          case "5" -> libraryService.borrowBook();
          case "6" -> libraryService.returnBookToLibrary();
          case "7" -> libraryService.showBorrowedBooks();
          case "8" -> libraryService.showCurrentReaderOfBook();
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
      if (showOptions) {
        System.out.println(SEPARATOR);
        System.out.println(OPTIONS_MESSAGE);
      }
    }
  }
  private void exitFromMenu(){
    System.out.println("Goodbye!");
    exitRequest = true;
    showOptions = false;
  }
}
