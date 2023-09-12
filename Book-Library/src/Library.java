import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private List<Reader> readers;

    public Library(){
        books = new ArrayList<>();
        readers = new ArrayList<>();

        books.add(new Book(1, "1984", "George Orwell"));
        books.add(new Book(2, "Home", "Tony Morrsion"));
        books.add(new Book(3, "Glue", "Irvine Welsh"));

        readers.add(new Reader(1, "Ivan"));
        readers.add(new Reader(2, "Yevhenii"));
        readers.add(new Reader(3, "Andrii"));
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Reader> getReaders() {
        return readers;
    }


}
