package service;

import exception.LibraryServiceException;

public class Validator {
  public void validateNewBookInputFormat(String book) {
    if (!book.matches("^\\s*[^/]+\\s*/\\s*[^/]+\\s*$")) {
      throw new LibraryServiceException(
          "Invalid input format. Please use letters and exactly one '/' character.");
    }
  }

  public void validateName(String name) {
    var NameValidatorCounter = 0;
    var nameLength = name.length();
    if (!name.matches(".*[a-zA-Z].*")) {
      NameValidatorCounter++;
    }

    if (!name.matches("^[a-zA-Z\\s'-]+$")) {
      NameValidatorCounter++;
    }

    if (nameLength < 5) {
      NameValidatorCounter++;
    }

    if (nameLength > 30) {
      NameValidatorCounter++;
    }
    if (NameValidatorCounter != 0) {
      throw new LibraryServiceException(
          "Name must be longer than 5 characters, shorter than 30 characters and must contain only ENGLISH letters, spaces, dashes, apostrophes");
    }
  }

  public void validateBookTitle(String bookTitle) {
    var BookTitleValidatorCounter = 0;
    var bookTitleLength = bookTitle.length();

    if (bookTitle.matches(".*[|/\\\\#%=+*_><].*")) {
      BookTitleValidatorCounter++;
    }

    if (bookTitleLength < 5) {
      BookTitleValidatorCounter++;
    }

    if (bookTitleLength > 100) {
      BookTitleValidatorCounter++;
    }
    if (BookTitleValidatorCounter != 0) {
      throw new LibraryServiceException(
          "Book title must be written using ENGLISH letters, longer than 5 characters, shorter than 100 characters and must not contain the following characters: |/\\#%=+*_><");
    }
  }

  public void validateIdToBorrowBook(String inputId) {
    String[] ids = inputId.split("/");
    var bookIdStr = ids[0].trim();
    var readerIdStr = ids[1].trim();

    if (!bookIdStr.matches("^[1-9]\\d*$") || !readerIdStr.matches("^[1-9]\\d*$")) {
      throw new LibraryServiceException("Book ID and Reader ID must be POSITIVE INT values");
    }
  }

  public void validateSingleId(String inputId) {
    if (!inputId.matches("^[1-9]\\d*\\s*$")) {
      throw new LibraryServiceException("Book ID must be only 1 POSITIVE INT int value");
    }
  }
}
