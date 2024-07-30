package com.example.booklibrary.controllers.integration;

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

    mockMvc
        .perform(
            post("/api/v1/readers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reader)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value(reader.getName()));
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
        .andExpect(jsonPath("$[0].readerId").value(reader1.getId()))
        .andExpect(jsonPath("$[1].name").value(book2.getName()))
        .andExpect(jsonPath("$[1].author").value(book2.getAuthor()))
        .andExpect(jsonPath("$[1].readerId").value(reader1.getId()));
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
        .andExpect(jsonPath("$[0].books[0].author").value(book1.getAuthor()))
        .andExpect(jsonPath("$[0].books[0].readerId").value(reader1.getId()));
  }

  private static Reader generateReader(String name) {
    return new Reader(name);
  }

  private static Book generateBook(String name, String author) {
    return new Book(name, author);
  }
}
