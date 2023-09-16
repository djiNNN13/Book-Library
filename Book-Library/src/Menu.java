import java.util.Scanner;

public class Menu {
    private Library library;

    public Menu(Library library) {
        this.library = library;
    }

    public void displayMenu() {
        System.out.println("WELCOME TO THE LIBRARY!\n\n" + "PLEASE, SELECT ONE OF THE FOLLOWING " +
                "ACTIONS BY TYPING THE OPTION'S NUMBER AND PRESSING ENTER KEY:\n" +
                "\t[1]SHOW ALL BOOKS IN THE LIBRARY\n" +
                "\t[2]SHOW ALL READERS REGISTERED IN THE LIBRARY\n\n" +
                "TYPE \"EXIT\" TO STOP THE PROGRAM AND EXIT! ");

        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        switch (option) {
            case "1" -> library.getBooks().stream().forEach(System.out::println);
            case "2" -> library.getReaders().stream().forEach(System.out::println);
            case "exit" -> {
                System.out.println("Goodbye");
                System.exit(0);
            }
            default -> {
                System.out.println("Invalid option");
                System.exit(0);
            }
        }
    }
}
