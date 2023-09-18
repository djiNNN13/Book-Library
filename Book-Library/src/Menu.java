import java.util.Scanner;

public class Menu {
  private static final String WELCOME_MESSAGE = "WELCOME TO THE LIBRARY!";
  private static final String OPTIONS_MESSAGE =
      """
                    PLEASE, SELECT ONE OF THE FOLLOWING
                    ACTIONS BY TYPING THE OPTION'S NUMBER AND PRESSING ENTER KEY:
                        [1]SHOW ALL BOOKS IN THE LIBRARY
                        [2]SHOW ALL READERS REGISTERED IN THE LIBRARY

                    TYPE "EXIT" TO STOP THE PROGRAM AND EXIT!
                    """;
  private static final String SEPARATOR =
      "-------------------------------------------------------------------";
  private final Library library = new Library();
  private final Scanner scanner = new Scanner(System.in);

  public void displayMenu() {
    boolean showOptions = true;
    System.out.println(SEPARATOR);
    System.out.println(WELCOME_MESSAGE + "\n\n" + OPTIONS_MESSAGE);
    boolean exitRequest = false;
    while (!exitRequest) {
      String option = scanner.nextLine().toLowerCase();
      switch (option) {
        case "1" -> showAllBooks();
        case "2" -> showAllReaders();
        case "exit" -> {
          System.out.println("Goodbye!");
          exitRequest = true;
          showOptions = false;
        }
        default -> System.out.println("Invalid option, please write correct option from the menu.");
      }
      if (showOptions) {
        System.out.println(SEPARATOR);
        System.out.println(OPTIONS_MESSAGE);
      }
    }
  }

  private void showAllBooks() {
    library.getBooks().forEach(System.out::println);
  }

  private void showAllReaders() {
    library.getReaders().forEach(System.out::println);
  }
}
