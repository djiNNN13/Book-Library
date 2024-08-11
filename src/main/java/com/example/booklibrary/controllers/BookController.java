package com.example.booklibrary.controllers;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.dto.BookWithReaderDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.ReaderNotFoundException;
import com.example.booklibrary.exception.SaveBookException;
import com.example.booklibrary.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Book API", description = "Endpoints for operations about book")
public class BookController {
  private final LibraryService libraryService;

  public BookController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @Operation(summary = "Get all books from the library", description = "Returns a list of books")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved",
      content =
          @Content(
              mediaType = "application/json",
              examples =
                  @ExampleObject(
                      description = "Example of returned books",
                      value =
                          """
                                          [
                                              {
                                                "id": 1,
                                                "name": "Glue",
                                                "author": "Irvine Welsh"
                                            },
                                              {
                                                "id": 2,
                                                "name": "1984",
                                                "author": "George Orwell"
                                            }
                                          ]
                                          """)))
  @GetMapping("/books")
  public ResponseEntity<List<BookDto>> getBooks() {
    var books = libraryService.findAllBooks();
    return ResponseEntity.ok(books);
  }

  @Operation(
      summary = "Add a new book to the library",
      description = "Save a new book to the library")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "id": 4,
                                    "name": "The Great Book",
                                    "author": "John Doe"
                                   }
                            """))),
    @ApiResponse(
        responseCode = "400",
        content =
            @Content(
                mediaType = "application/json",
                examples = {
                  @ExampleObject(
                      name = "Contains ID",
                      description = "Request body contains book id",
                      value =
                          """
                                {
                              "localDateTime": "2024-08-05T16:16:53.8490207",
                              "errorMessage": "Request body should not contain book id value"
                            }
                            """),
                  @ExampleObject(
                      name = "Contains invalid fields",
                      description = "Request body contains invalid fields",
                      value =
                          """
                            [
                            {
                             "localDateTime": "2024-08-06T16:48:58.1598451",
                             "errorMessage": "Failed to create a new book, the request contains invalid fields",
                             "errors":[
                             {
                             "field": "author",
                             "message": "Book author must contain only ENGLISH letters, spaces, dashes, apostrophes",
                             "rejectedValue": "123"
                             },
                             {
                             "field": "author",
                             "message": "Book author must be longer than 5 characters, shorter than 30 characters",
                             "rejectedValue": "123"
                             },
                             {
                             "field": "name",
                             "message": "Book name must be longer than 5 characters, shorter than 100 characters",
                             "rejectedValue": "asd"
                             }
                             ]
                             }
                             ]
                                    """)
                }))
  })
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Book to save in the library",
      content =
          @Content(
              mediaType = "application/json",
              examples =
                  @ExampleObject(
                      value =
                          """
                                      {
                                    "name": "The Great Book",
                                    "author": "John Doe"
                                   }
                                  """)))
  @PostMapping("/books")
  public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
    if (book.getId() != null) {
      throw new SaveBookException("Request body should not contain book id value");
    }
    var savedBook = libraryService.addNewBook(book);
    return ResponseEntity.ok(savedBook);
  }

  @Operation(
      summary = "Borrow book to reader",
      description = "Both id's should exist and be positive")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully borrowed"),
    @ApiResponse(
        responseCode = "400",
        content =
            @Content(
                mediaType = "application/json",
                examples = {
                  @ExampleObject(
                      name = "Invalid IDs",
                      description = "Both IDs are negative",
                      value =
                          """
                              {
                              "localDateTime": "2024-08-05T17:21:09.9245848",
                              "errorMessage": "Book ID must be a positive number, Reader ID must be a positive number"
                              }
                            """),
                  @ExampleObject(
                      name = "Book doesn't exists",
                      description = "This book doesn't exists in the library",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T19:45:31.1149262",
                            "errorMessage": "This Book ID doesn't exist!"
                            }
                          """),
                  @ExampleObject(
                      name = "Reader doesn't exists",
                      description = "This reader doesn't exists in the library",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T19:45:31.1149262",
                            "errorMessage": "This Reader ID doesn't exist!"
                            }
                          """),
                  @ExampleObject(
                      name = "Book isn't available",
                      description = "Book is already borrowed by other reader",
                      value =
                          """
                            {
                             "localDateTime": "2024-08-05T19:46:59.2143248",
                             "errorMessage": "Cannot borrow already borrowed Book!"
                             }
                          """),
                }))
  })
  @PostMapping("/books/{bookId}/readers/{readerId}")
  public ResponseEntity<Void> borrowBookToReader(
      @PathVariable("bookId")
          @NotNull
          @Positive(message = "Book ID must be a positive number")
          @Parameter(description = "Book ID to borrow", example = "1")
          Long bookId,
      @PathVariable("readerId")
          @NotNull
          @Positive(message = "Reader ID must be a positive number")
          @Parameter(description = "Reader ID to borrow", example = "1")
          Long readerId) {
    libraryService.borrowBook(bookId, readerId);
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Return book to the library",
      description = "Book ID must exists in the library and be positive")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully returned"),
    @ApiResponse(
        responseCode = "400",
        content =
            @Content(
                mediaType = "application/json",
                examples = {
                  @ExampleObject(
                      name = "Invalid ID",
                      description = "Book ID is negative",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T18:28:33.2084866",
                            "errorMessage": "Book ID must be a positive number"
                            }
                          """),
                  @ExampleObject(
                      name = "Book doesn't exists",
                      description = "This book ID doesn't exists in the library",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T18:28:43.7529715",
                            "errorMessage": "This Book ID doesn't exist!"
                            }
                          """),
                  @ExampleObject(
                      name = "Book hasn't reader",
                      description = "Book already in the library",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T18:29:23.9543926",
                            "errorMessage": "Cannot return Book. Book is already in the Library!"
                            }
                          """)
                }))
  })
  @DeleteMapping("/books/{bookId}")
  public ResponseEntity<Void> returnBook(
      @PathVariable("bookId")
          @NotNull
          @Positive(message = "Book ID must be a positive number")
          @Parameter(
              name = "Book ID",
              description = "Book ID to return",
              example = "1",
              required = true)
          Long bookId) {
    libraryService.returnBookToLibrary(bookId);
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Get reader by book ID",
      description = "Book ID must exists, be positive and hasn't reader")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                "id": 1,
                                "name": "Ivan"
                                }
                            """))),
    @ApiResponse(
        responseCode = "400",
        content =
            @Content(
                mediaType = "application/json",
                examples = {
                  @ExampleObject(
                      name = "Invalid ID",
                      description = "Book ID is negative",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T17:48:44.2293128",
                            "errorMessage": "Book ID must be a positive number"
                            }
                        """),
                  @ExampleObject(
                      name = "Book without reader",
                      description = "Book ID hasn't any reader from the library",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T17:54:48.2300445",
                            "errorMessage": "Cannot find reader by book = 4 id! Book has not any reader"
                            }
                          """),
                  @ExampleObject(
                      name = "Book doesn't exists",
                      description = "This book ID doesn't exists in the library",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T18:21:51.3133578",
                            "errorMessage": "This Book ID doesn't exist!"
                            }
                          """)
                }))
  })
  @GetMapping("/books/{bookId}/reader")
  public ResponseEntity<Reader> getReaderByBookId(
      @PathVariable("bookId")
          @NotNull
          @Positive(message = "Book ID must be a positive number")
          @Parameter(description = "Book ID to get reader", example = "1")
          Long bookId) {
    return libraryService
        .showCurrentReaderOfBook(bookId)
        .map(ResponseEntity::ok)
        .orElseThrow(
            () ->
                new ReaderNotFoundException(
                    String.format(
                        "Cannot find reader by book = %d id! Book has not any reader", bookId)));
  }

  @Operation(
      summary = "Get all books with readers",
      description = "Returns list of all books and their readers from the library")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved",
      content =
          @Content(
              mediaType = "application/json",
              examples =
                  @ExampleObject(
                      value =
                          """
                                          [
                                           {
                                           "id": 3,
                                           "author": "Irvine Welsh",
                                           "name": "Glue",
                                           "reader":{
                                           "id": 1,
                                           "name": "Ivan"
                                           }
                                           },
                                           {
                                           "id": 2,
                                           "author": "Tony Morrison",
                                           "name": "Home",
                                           "reader":{
                                           "id": 2,
                                           "name": "Yevhenii"
                                           }
                                           },
                                           {
                                           "id": 1,
                                           "author": "George Orwell",
                                           "name": "1984",
                                           "reader":{
                                           "id": 1,
                                           "name": "Ivan"
                                           }
                                           }
                                           ]
                                          """)))
  @GetMapping("/books/readers")
  public ResponseEntity<List<BookWithReaderDto>> getBooksWithReaders() {
    var booksWithReaders = libraryService.findAllBooksWithReaders();
    return ResponseEntity.ok(booksWithReaders);
  }
}
