import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibraryService {
  private final Validator validator = new Validator();
  private final Library library;

  public LibraryService(Library library) {
    this.library = library;
  }

  public void showAllBooks() {
    library.getBooks().forEach(System.out::println);
  }

  public void showAllReaders() {
    library.getReaders().forEach(System.out::println);
  }

  private Optional<Reader> findReaderForBook(int bookId) {
    return library.getBorrowedBooks().entrySet().stream()
        .filter(entry -> entry.getKey().getId() == bookId)
        .map(Map.Entry::getValue)
        .findFirst();
  }

  private List<Book> findBorrowedBooksForReader(int readerId) {
    return library.getBorrowedBooks().entrySet().stream()
        .filter(entry -> entry.getValue().getId() == readerId)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  public void showCurrentReaderOfBook(String userInput) throws InvalidIdException {
    validator.validateSingleId(userInput);
    int bookId = Integer.parseInt(userInput);
    Optional<Reader> reader = findReaderForBook(bookId);
    if (reader.isPresent()) {
      System.out.println("Borrowed by reader ID: " + reader.get().getId());
    } else {
      System.err.println("No readers found for Book ID = " + bookId);
    }
  }

  public void showBorrowedBooks(String userInput) throws InvalidIdException {
    validator.validateSingleId(userInput);
    int readerId = Integer.parseInt(userInput);
    List<Book> borrowedBooks = findBorrowedBooksForReader(readerId);
    if (!borrowedBooks.isEmpty()) {
      for (Book book : borrowedBooks) {
        System.out.println(
            "Title: "
                + book.getName()
                + ", Author: "
                + book.getAuthor()
                + ", Borrowed by reader ID: "
                + readerId);
      }
    } else {
      System.err.println("No borrowed books found for Reader ID = " + readerId);
    }
  }

  public void registerNewReader(String readerName) throws InvalidNameException {
    validator.validateName(readerName);
    library.addReader(new Reader(readerName));
  }

  public void addNewBook(String book) throws InvalidBookTitleException, InvalidNameException {
    if (!book.matches("^.*\\/.*$")) {
      System.err.println("Invalid input format. Please use letters and exactly one '/' character.");
      return;
    }
    String[] parts = book.split("/");
    String bookTitle = parts[0];
    String authorName = parts[1];
    validator.validateBookTitle(bookTitle);
    validator.validateName(authorName);
    library.addBook(new Book(bookTitle, authorName));
  }

  public void borrowBook(String userInput) throws InvalidInputFormatException, InvalidIdException {
    if (!userInput.matches("^[^/]*[/][^/]*$")) {
      System.err.println("Invalid input format. Please use exactly one '/' character.");
      return;
    }
    String[] ids = userInput.split("/");
    validator.validateIdToBorrowBook(userInput);
    int bookId = Integer.parseInt(ids[0]);
    int readerId = Integer.parseInt(ids[1]);

    Optional<Book> matchingBook =
        library.getBooks().stream().filter(book -> bookId == book.getId()).findFirst();
    if (matchingBook.isEmpty()) {
      System.err.println("Book with ID " + bookId + " not found!");
      return;
    }
    Book bookToBorrow = matchingBook.get();
    Optional<Reader> matchingReader =
        library.getReaders().stream().filter(reader -> readerId == reader.getId()).findFirst();
    if (matchingReader.isEmpty()) {
      System.err.println("Reader with ID " + readerId + " not found!");
      return;
    }
    Reader borrowingReader = matchingReader.get();

    library.removeBook(bookToBorrow);
    library.addBorrowedBook(bookToBorrow, borrowingReader);
    System.out.println(library.getBorrowedBooks());
  }

  public void returnBookToLibrary(String userInput) throws InvalidIdException {
    validator.validateSingleId(userInput);
    int bookId = Integer.parseInt(userInput);
    Book bookToReturn =
        library.getBorrowedBooks().keySet().stream()
            .filter(book -> book.getId() == bookId)
            .findFirst()
            .orElse(null);
    if (bookToReturn == null) {
      System.err.println("Book with ID " + bookId + " not found!");
      return;
    }
    library.addBook(bookToReturn);
    library.removeBorrowedBook(bookToReturn);
    System.out.println(library.getBorrowedBooks());
  }
}
