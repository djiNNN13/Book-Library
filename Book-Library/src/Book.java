public class Book {
    private int id;
    private String Name;
    private String Author;

    public Book(int id, String name, String author) {
        this.id = id;
        Name = name;
        Author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getAuthor() {
        return Author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", Author='" + Author + '\'' +
                '}';
    }
}
