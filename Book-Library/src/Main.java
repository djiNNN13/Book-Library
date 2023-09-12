import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Menu menu = new Menu(new Library());
        menu.displayMenu();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toLowerCase();
        menu.executeOption(input);


    }
}