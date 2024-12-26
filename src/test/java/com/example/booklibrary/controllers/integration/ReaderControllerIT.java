package com.example.booklibrary.controllers.integration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booklibrary.controllers.integration.annotation.ControllerIT;
import com.example.booklibrary.dao.BookDao;
import com.example.booklibrary.dao.ReaderDao;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ControllerIT
class ReaderControllerIT {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ReaderDao readerDao;
  @Autowired private BookDao bookDao;

  @Test
  void getReaders() throws Exception {
    var reader1 = readerDao.save(generateReader("Ivan"));
    var reader2 = readerDao.save(generateReader("Yevhenii"));
    var reader3 = readerDao.save(generateReader("Andrii"));

    mockMvc
        .perform(get("/api/v1/readers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(reader1.getId()))
        .andExpect(jsonPath("$[0].name").value(reader1.getName()))
        .andExpect(jsonPath("$[1].id").value(reader2.getId()))
        .andExpect(jsonPath("$[1].name").value(reader2.getName()))
        .andExpect(jsonPath("$[2].id").value(reader3.getId()))
        .andExpect(jsonPath("$[2].name").value(reader3.getName()));
  }

  @Test
  void saveReader() throws Exception {
    var reader = generateReader("Yevhenii");

    var isReaderPresent =
        readerDao.findAll().stream().anyMatch(currentReader -> currentReader.equals(reader));
    assertThat(isReaderPresent).isFalse();

    var result =
        mockMvc
            .perform(
                post("/api/v1/readers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reader)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(reader.getName()))
            .andReturn();

    var jsonResponse = result.getResponse().getContentAsString();
    var createdReader = objectMapper.readValue(jsonResponse, Reader.class);

    assertAll(
        () -> assertThat(readerDao.findById(createdReader.getId())).isPresent(),
        () -> assertThat(createdReader).isEqualTo(readerDao.findById(createdReader.getId()).get()));
  }

  @Test
  void saveReaderWithInvalidRequestBody() throws Exception {
    var reader = new Reader(4L, "Yevhenii");

    mockMvc
        .perform(
            post("/api/v1/readers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reader)))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errorMessage").value("Request body should not contain reader id value"));

    Optional<Reader> maybeReader = readerDao.findById(reader.getId());
    assertThat(maybeReader).isNotPresent();
  }

  @Test
  void getBorrowedBooksByReaderId() throws Exception {
    var book1 = bookDao.save(generateBook("Martin Eden", "Jack London"));
    var book2 = bookDao.save(generateBook("1984", "George Orwell"));
    var book3 = bookDao.save(generateBook("Home", "Tony Morrison"));
    var reader1 = readerDao.save(generateReader("Jonny"));
    var reader2 = readerDao.save(generateReader("Yevhenii"));

    bookDao.borrow(book1.getId(), reader1.getId());
    bookDao.borrow(book2.getId(), reader1.getId());
    bookDao.borrow(book3.getId(), reader2.getId());

    mockMvc
        .perform(get("/api/v1/readers/{readerId}/books", reader1.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value(book1.getName()))
        .andExpect(jsonPath("$[0].author").value(book1.getAuthor()))
        .andExpect(jsonPath("$[1].name").value(book2.getName()))
        .andExpect(jsonPath("$[1].author").value(book2.getAuthor()));
  }

  @Test
  void getReadersWithBorrowedBooks() throws Exception {
    var book1 = bookDao.save(generateBook("Martin Eden", "Jack London"));
    var reader1 = readerDao.save(generateReader("Jonny"));

    bookDao.borrow(book1.getId(), reader1.getId());

    mockMvc
        .perform(get("/api/v1/readers/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(reader1.getId()))
        .andExpect(jsonPath("$[0].name").value(reader1.getName()))
        .andExpect(jsonPath("$[0].books.length()").value(1))
        .andExpect(jsonPath("$[0].books[0].id").value(book1.getId()))
        .andExpect(jsonPath("$[0].books[0].name").value(book1.getName()))
        .andExpect(jsonPath("$[0].books[0].author").value(book1.getAuthor()));
  }

  private static Book generateBook(String name, String author) {
    return Book.builder().name(name).author(author).build();
  }

  private static Reader generateReader(String name) {
    return Reader.builder().name(name).build();
  }
}
