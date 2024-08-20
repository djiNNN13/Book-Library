package com.example.booklibrary.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.dto.ReaderWithBooksDto;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ReaderController.class)
@ExtendWith(MockitoExtension.class)
class ReaderControllerTest {
  private static final String NAME_LENGTH_ERROR_MESSAGE =
      "Reader name must be longer than 5 characters, shorter than 30 characters";
  private static final String NAME_CHARACTER_ERROR_MESSAGE =
      "Reader name must contain only ENGLISH letters, spaces, dashes, apostrophes";
  @Autowired ReaderController readerController;
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;
  @MockBean LibraryService libraryService;

  @Test
  void getReadersShouldReturnReaderList() throws Exception {
    var readerList =
        List.of(
            generateReaderWithId(1L, "Yevhenii"),
            generateReaderWithId(2L, "Alex"),
            generateReaderWithId(3L, "Andrew"));

    when(libraryService.findAllReader()).thenReturn(readerList);

    mockMvc
        .perform(get("/api/v1/readers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].name").value("Yevhenii"))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].name").value("Alex"))
        .andExpect(jsonPath("$[2].id").value(3L))
        .andExpect(jsonPath("$[2].name").value("Andrew"));
  }

  @Test
  void getReadersShouldReturnEmptyList() throws Exception {
    List<Reader> readerList = List.of();
    when(libraryService.findAllReader()).thenReturn(readerList);

    mockMvc
        .perform(get("/api/v1/readers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void saveReader() throws Exception {
    var reader = generateReader("Test Name");
    var readerJson = objectMapper.writeValueAsString(reader);
    var savedReader = generateReaderWithId(1L, "Test Name");

    when(libraryService.addNewReader(reader)).thenReturn(savedReader);

    mockMvc
        .perform(
            post("/api/v1/readers").contentType(MediaType.APPLICATION_JSON).content(readerJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(savedReader.getId()))
        .andExpect(jsonPath("$.name").value("Test Name"));

    verify(libraryService, times(1)).addNewReader(reader);
  }

  @Test
  void saveReaderShouldThrowsExceptionIfRequestBodyContainsId() throws Exception {
    var reader = generateReaderWithId(1L, "Test Name");
    var readerJson = objectMapper.writeValueAsString(reader);

    mockMvc
        .perform(
            post("/api/v1/readers").contentType(MediaType.APPLICATION_JSON).content(readerJson))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errorMessage").value("Request body should not contain reader id value"));

    verify(libraryService, never()).addNewReader(reader);
  }

  private static Stream<Arguments> provideInvalidReaderNameAndErrorMessage() {
    return Stream.of(
        Arguments.of("Reader Name/", NAME_CHARACTER_ERROR_MESSAGE),
        Arguments.of("Имя Читателя", NAME_CHARACTER_ERROR_MESSAGE),
        Arguments.of("x", NAME_LENGTH_ERROR_MESSAGE),
        Arguments.of("dbhxqvdxujryfskzaewgwtzptkgdoga", NAME_LENGTH_ERROR_MESSAGE));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidReaderNameAndErrorMessage")
  void saveReaderShouldThrowsExceptionIfInvalidArguments(String name, String errorMessage)
      throws Exception {
    var reader = generateReader(name);
    var readerJson = objectMapper.writeValueAsString(reader);

    mockMvc
        .perform(
            post("/api/v1/readers").contentType(MediaType.APPLICATION_JSON).content(readerJson))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errorMessage")
                .value("Failed to create a new reader, the request contains invalid fields"))
        .andExpect(jsonPath("$.errors[0].message").value(errorMessage));

    verify(libraryService, never()).addNewReader(reader);
  }

  @Test
  void getBorrowedBooksByReaderId() throws Exception {
    var bookList =
        List.of(
            new BookDto(1L, "Test1", "Test1"),
            new BookDto(2L, "Test2", "Test2"),
            new BookDto(3L, "Test3", "Test3"));

    when(libraryService.showBorrowedBooks(1L)).thenReturn(bookList);

    mockMvc
        .perform(get("/api/v1/readers/{readerId}/books", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].id").value(bookList.get(0).getId()))
        .andExpect(jsonPath("$[0].name").value(bookList.get(0).getName()))
        .andExpect(jsonPath("$[0].author").value(bookList.get(0).getAuthor()))
        .andExpect(jsonPath("$[1].id").value(bookList.get(1).getId()))
        .andExpect(jsonPath("$[1].name").value(bookList.get(1).getName()))
        .andExpect(jsonPath("$[1].author").value(bookList.get(1).getAuthor()))
        .andExpect(jsonPath("$[2].id").value(bookList.get(2).getId()))
        .andExpect(jsonPath("$[2].name").value(bookList.get(2).getName()))
        .andExpect(jsonPath("$[2].author").value(bookList.get(2).getAuthor()));

    verify(libraryService, times(1)).showBorrowedBooks(1L);
  }

  @Test
  void getBorrowedBooksByReaderIdShouldReturnEmptyBookList() throws Exception {
    List<BookDto> bookList = List.of();

    when(libraryService.showBorrowedBooks(1L)).thenReturn(bookList);

    mockMvc
        .perform(get("/api/v1/readers/{readerId}/books", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));

    verify(libraryService, times(1)).showBorrowedBooks(1L);
  }

  @Test
  void getBorrowedBooksByReaderIdShouldThrowsExceptionIfInvalidId() throws Exception {
    var expectedMessage = "Reader ID must be a positive number";

    mockMvc
        .perform(get("/api/v1/readers/{readerId}/books", -1L))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").value(expectedMessage));

    verify(libraryService, never()).showBorrowedBooks(-1L);
  }

  @Test
  void getReadersWithBorrowedBooks() throws Exception {
    List<ReaderWithBooksDto> readerWithBooks =
        List.of(
            new ReaderWithBooksDto(
                1L,
                "Reader1",
                List.of(new BookDto(1L, "Book1", "Book1"), new BookDto(2L, "Book2", "Book2"))),
            new ReaderWithBooksDto(
                2L, "Reader2", Collections.singletonList(new BookDto(3L, "Book3", "Book3"))),
            new ReaderWithBooksDto(3L, "Reader3", Collections.emptyList()));

    when(libraryService.findAllReadersWithBooks()).thenReturn(readerWithBooks);

    mockMvc
        .perform(get("/api/v1/readers/books"))
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].id").value(readerWithBooks.get(0).getId()))
        .andExpect(jsonPath("$[0].name").value(readerWithBooks.get(0).getName()))
        .andExpect(jsonPath("$[0].books[0].length()").value(3))
        .andExpect(jsonPath("$[1].id").value(readerWithBooks.get(1).getId()))
        .andExpect(jsonPath("$[1].name").value(readerWithBooks.get(1).getName()))
        .andExpect(jsonPath("$[1].books.length()").value(1))
        .andExpect(jsonPath("$[2].id").value(readerWithBooks.get(2).getId()))
        .andExpect(jsonPath("$[2].name").value(readerWithBooks.get(2).getName()))
        .andExpect(jsonPath("$[2].books.length()").value(0));
  }

  private static Reader generateReader(String name) {
    return Reader.builder().name(name).build();
  }

  private static Reader generateReaderWithId(Long id, String name) {
    return Reader.builder().id(id).name(name).build();
  }
}
