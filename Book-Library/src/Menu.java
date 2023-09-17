import java.util.Scanner;

public class Menu {
    private Library library;

    private Scanner scanner;
    private String welcomeMessage;
    private String optionsMessage;

    private String separator = "-------------------------------------------------------------------";

    public Menu() {
        library = new Library();
        welcomeMessage = "WELCOME TO THE LIBRARY!";
        optionsMessage = """
                PLEASE, SELECT ONE OF THE FOLLOWING 
                ACTIONS BY TYPING THE OPTION'S NUMBER AND PRESSING ENTER KEY:
                    [1]SHOW ALL BOOKS IN THE LIBRARY
                    [2]SHOW ALL READERS REGISTERED IN THE LIBRARY
                            
                TYPE "EXIT" TO STOP THE PROGRAM AND EXIT! 
                """;
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {

        System.out.println(separator);
        System.out.println(welcomeMessage + "\n\n" + optionsMessage);
        boolean exitRequset = false;

        while (!exitRequset) {
            String option = scanner.nextLine().toLowerCase();

            printSeparatorAndOptionsMessage();
            switch (option) {
                case "1" -> showAllBooks();

                case "2" -> showAllReaders();

                case "exit" -> exitRequset = exit();

                default -> {
                    System.out.println("Invalid option, please write correct option from the menu.");
                    System.out.println(separator);
                }

            }
        }
    }

    private void printSeparatorAndOptionsMessage() {
        System.out.println(separator);
        System.out.println(optionsMessage);
    }

    private void showAllBooks() {
        library.getBooks().forEach(System.out::println);
    }

    private void showAllReaders() {
        library.getReaders().forEach(System.out::println);
    }

    private boolean exit() {
        System.out.println("Goodbye");
        System.out.println(separator);
        return true;
    }


}

