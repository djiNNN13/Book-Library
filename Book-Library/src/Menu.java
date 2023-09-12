public class Menu {
    private Library library;

    public Menu(Library library) {
        this.library = library;
    }

    public void displayMenu(){
        System.out.println("WELCOME TO THE LIBRARY!\n\n" + "PLEASE, SELECT ONE OF THE FOLLOWING " +
                "ACTIONS BY TYPING THE OPTION'S NUMBER AND PRESSING ENTER KEY:\n" +
                "\t[1]SHOW ALL BOOKS IN THE LIBRARY\n" +
                "\t[2]SHOW ALL READERS REGISTERED IN THE LIBRARY\n\n" +
                "TYPE \"EXIT\" TO STOP THE PROGRAM AND EXIT! ");
    }
    public void executeOption(String option){
        switch (option){
            case "1":
                System.out.println(library.getBooks());
                break;
            case "2":
                System.out.println(library.getReaders());
                break;
            case "exit":
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid option");
        }
    }
}
