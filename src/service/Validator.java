package service;

import exception.InvalidBookTitleException;
import exception.InvalidIdException;
import exception.InvalidInputFormatException;
import exception.InvalidNameException;

public class Validator {

  public void validateName(String name) throws InvalidNameException {
    int nameLength = name.length();

    if (!name.matches(".*[a-zA-Z].*")) {
      throw new InvalidNameException("Name must contain at least one English letter!");
    }

    if (!name.matches("^[a-zA-Z\\s'-]+$")) {
      throw new InvalidNameException(
          "Name must contain only English letters, spaces, dashes, and apostrophes!");
    }

    if (nameLength < 5) {
      throw new InvalidNameException("Name must be longer than 5 characters!");
    }

    if (nameLength > 30) {
      throw new InvalidNameException("Name must be shorter than 30 characters!");
    }
  }

  public void validateBookTitle(String bookTitle) throws InvalidBookTitleException {
    int bookTitleLength = bookTitle.length();

    if (bookTitle.matches(".*[|/\\\\#%=+*_><].*")) {
      throw new InvalidBookTitleException(
          "Book title must not contain the following characters: |/\\#%=+*_><");
    }

    if (bookTitleLength < 5) {
      throw new InvalidBookTitleException("Book title must be longer than 5 characters!");
    }

    if (bookTitleLength > 100) {
      throw new InvalidBookTitleException("Book title must be shorter than 100 characters!");
    }
  }

  public void validateIdToBorrowBook(String inputId)
      throws InvalidInputFormatException, InvalidIdException {
    if (!inputId.matches("^[^/]*[/][^/]*$")) {
      throw new InvalidInputFormatException(
          "Invalid input format. Please use exactly one '/' character.");
    }

    String[] ids = inputId.split("/");
    String bookIdStr = ids[0];
    String readerIdStr = ids[1];

    if (!bookIdStr.matches("\\d+") || !readerIdStr.matches("\\d+")) {
      throw new InvalidIdException("IDs must be positive int!");
    }
  }

  public void validateSingleId(String inputId) throws InvalidIdException {
    if (!inputId.matches("^-?\\d+$")) {
      throw new InvalidIdException("Book ID must be only 1 int value!");
    }
  }
}
