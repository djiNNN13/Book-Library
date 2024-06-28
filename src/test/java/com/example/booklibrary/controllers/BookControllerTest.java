package com.example.booklibrary.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.booklibrary.dto.BookWithReaderDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.ReaderNotFoundException;
import com.example.booklibrary.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookController.class)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {
  @Autowired BookController bookController;
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;
  @MockBean LibraryService libraryService;

  @Test
  void getBooksShouldReturnBookList() throws Exception {
    var bookList =
        List.of(
            generateBook(1L, "Test1", "Test1"),
            generateBook(2L, "Test2", "Test2"),
            generateBook(3L, "Test3", "Test3"));

    when(libraryService.findAllBooks()).thenReturn(bookList);

    mockMvc
        .perform(get("/api/v1/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));
  }

  @Test
  void getBooksShouldReturnEmptyList() throws Exception {
    List<Book> bookList = new ArrayList<>();

    when(libraryService.findAllBooks()).thenReturn(bookList);

    mockMvc
        .perform(get("/api/v1/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void saveBookShouldReturnSavedBook() throws Exception {
    var book = generateBook(1L, "dummy", "dummy");
    var bookJson = objectMapper.writeValueAsString(book);

    when(libraryService.addNewBook(book)).thenReturn(book);

    mockMvc
        .perform(post("/api/v1/books").contentType(MediaType.APPLICATION_JSON).content(bookJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("dummy"))
        .andExpect(jsonPath("$.author").value("dummy"))
        .andExpect(jsonPath("$.readerId").doesNotExist());

    verify(libraryService, times(1)).addNewBook(book);
  }

  @Test
  void borrowBookToReader() throws Exception {
    var bookId = 1L;
    var readerId = 1L;

    doNothing().when(libraryService).borrowBook(bookId, readerId);

    mockMvc
        .perform(post("/api/v1/books/{bookId}/readers/{readerId}", bookId, readerId))
        .andExpect(status().isOk());
  }

  @ParameterizedTest
  @CsvSource({"-1, 1", "1, -1", "-1, -1"})
  void borrowBookToReaderShouldThrowsExceptionIfInvalidArguments(Long bookId, Long readerId)
      throws Exception {

    doNothing().when(libraryService).borrowBook(bookId, readerId);

    mockMvc
        .perform(post("/api/v1/books/{bookId}/readers/{readerId}", bookId, readerId))
        .andExpect(status().isBadRequest());
  }

  @Test
  void returnBook() throws Exception {
    var bookId = 1L;

    doNothing().when(libraryService).returnBookToLibrary(bookId);

    mockMvc.perform(delete("/api/v1/books/{bookId}", bookId)).andExpect(status().isOk());
  }

  @Test
  void getReaderByBookId() throws Exception {
    var bookId = 1L;
    var reader = generateReader(1L, "Test1");

    when(libraryService.showCurrentReaderOfBook(bookId)).thenReturn(Optional.of(reader));

    mockMvc
        .perform(get("/api/v1/books/{bookId}/reader", bookId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Test1"));
  }

  @Test
  void getReaderByBookIdShouldThrowsExceptionIfBookHasNotReader() throws Exception {
    var bookId = 1L;
    var errorMessage = "Cannot find reader by book = 1 id! Book has not any reader";
    when(libraryService.showCurrentReaderOfBook(bookId))
        .thenThrow(new ReaderNotFoundException(errorMessage));

    mockMvc
        .perform(get("/api/v1/books/{bookId}/reader", bookId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").value(errorMessage));
  }

  @Test
  void getBooksWithReader() throws Exception {
    List<BookWithReaderDto> booksWithReader =
        List.of(
            new BookWithReaderDto(1L, "Test1", "Test1", generateReader(1L, "Reader1")),
            new BookWithReaderDto(2L, "Test2", "Test2", generateReader(2L, "Reader2")));

    when(libraryService.findAllBooksWithReaders()).thenReturn(booksWithReader);

    mockMvc
        .perform(get("/api/v1/books/readers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(booksWithReader.get(0).getId()))
        .andExpect(jsonPath("$[0].author").value(booksWithReader.get(0).getAuthor()))
        .andExpect(jsonPath("$[0].name").value(booksWithReader.get(0).getName()))
        .andExpect(jsonPath("$[0].reader.id").value(booksWithReader.get(0).getReader().getId()))
        .andExpect(jsonPath("$[0].reader.name").value(booksWithReader.get(0).getReader().getName()))
        .andExpect(jsonPath("$[1].id").value(booksWithReader.get(1).getId()))
        .andExpect(jsonPath("$[1].author").value(booksWithReader.get(1).getAuthor()))
        .andExpect(jsonPath("$[1].name").value(booksWithReader.get(1).getName()))
        .andExpect(jsonPath("$[1].reader.id").value(booksWithReader.get(1).getReader().getId()))
        .andExpect(
            jsonPath("$[1].reader.name").value(booksWithReader.get(1).getReader().getName()));
  }

  private static Book generateBook(Long id, String name, String author) {
    return new Book(id, name, author);
  }

  private static Reader generateReader(Long id, String name) {
    return new Reader(id, name);
  }
}
