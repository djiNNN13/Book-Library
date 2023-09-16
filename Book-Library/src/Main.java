import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Menu menu = new Menu(new Library());
        while (true) {
            menu.displayMenu();
        }
    }
}