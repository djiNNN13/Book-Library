package com.example.booklibrary.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.booklibrary.exception.LibraryServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidatorTest {
  private final Validator validator = new Validator();

  @Test
  void validateNewBookInputFormatIfBookIsValid() {
    assertDoesNotThrow(() -> validator.validateNewBookInputFormat("Harry Potter / J.K. Rowling"));
  }

  @ParameterizedTest
  @CsvSource({"dummy dummy", "dummy//dummy"})
  void shouldThrowExceptionIfBookFormatIsInvalid(String book) {
    var exception =
        assertThrows(
            LibraryServiceException.class, () -> validator.validateNewBookInputFormat(book));
    assertThat(exception.getMessage())
        .isEqualTo("Invalid input format. Please use letters and exactly one '/' character");
  }

  @Test
  void validateNameIfNameIsValid() {
    assertDoesNotThrow(() -> validator.validateName(" Yevhenii-'"));
  }

  @ParameterizedTest
  @CsvSource({"x", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", "dummy<", "пример"})
  void shouldThrowExceptionIfNameIsInvalid(String name) {
    var exception = assertThrows(LibraryServiceException.class, () -> validator.validateName(name));
    assertThat(exception.getMessage())
        .isEqualTo(
            "Name must be longer than 5 characters, shorter than 30 characters"
                + " and must contain only ENGLISH letters, spaces, dashes, apostrophes");
  }

  @Test
  void validateBookTitleIfTitleIsValid() {
    assertDoesNotThrow(() -> validator.validateBookTitle("Martin Eden"));
  }

  @ParameterizedTest
  @CsvSource({
    "x",
    "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij",
    "dummy|",
    "dummy/",
    "dummy\\",
    "dummy#",
    "dummy%",
    "dummy=",
    "dummy+",
    "dummy*",
    "dummy_",
    "dummy>",
    "dummy<",
  })
  void shouldThrowExceptionIfBookTitleIsInvalid(String bookTitle) {
    var exception =
        assertThrows(LibraryServiceException.class, () -> validator.validateBookTitle(bookTitle));
    assertThat(exception.getMessage())
        .isEqualTo(
            "Book title must be written using ENGLISH letters, longer than 5 characters, "
                + "shorter than 100 characters and must not contain the following characters: |/\\#%=+*_><");
  }

  @Test
  void validateIdToBorrowBook() {
    assertDoesNotThrow(() -> validator.validateIdToBorrowBook("1/1"));
  }

  @ParameterizedTest
  @CsvSource({
    "1 1, Invalid input format. Please use INTEGERS and exactly one '/' character",
    "-1/-1, Book ID and Reader ID must be POSITIVE INT values"
  })
  void shouldThrowExceptionIfBorrowBookIdIsNotValid(String borrowIds, String exceptionMessage) {
    var exception =
        assertThrows(
            LibraryServiceException.class, () -> validator.validateIdToBorrowBook(borrowIds));
    assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
  }

  @Test
  void validateSingleId() {
    assertDoesNotThrow(() -> validator.validateSingleId("1"));
  }

  @Test
  void shouldThrowExceptionIfIdIsNegative() {
    var exception =
        assertThrows(LibraryServiceException.class, () -> validator.validateSingleId("-1"));
    assertThat(exception.getMessage()).isEqualTo("Book ID must be only 1 POSITIVE INT int value");
  }
}
