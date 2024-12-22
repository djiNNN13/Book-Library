package com.example.booklibrary.controllers;

import com.example.booklibrary.dto.BookDto;
import com.example.booklibrary.dto.ReaderWithBooksDto;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.SaveReaderException;
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

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Reader API", description = "Endpoints for operations about reader")
public class ReaderController {
  private final LibraryService libraryService;

  @Operation(summary = "Get all readers", description = "Returns all readers from the library")
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
                            "id": 1,
                            "name": "Ivan"
                            },
                            {
                            "id": 2,
                            "name": "Yevhenii"
                            }
                            ]
                            """)))
  @GetMapping("/readers")
  public ResponseEntity<List<Reader>> getReaders() {
    var readers = libraryService.findAllReader();
    return ResponseEntity.ok(readers);
  }

  @Operation(
      summary = "Add a new reader to the library",
      description = "Save a new reader to the library")
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
                                "name": "Misha"
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
                            "localDateTime": "2024-08-05T18:53:24.8706155",
                            "errorMessage": "Request body should not contain reader id value"
                            }
                          """),
                  @ExampleObject(
                      name = "Contains invalid fields",
                      description = "Request body contains invalid fields",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T18:53:58.4462374",
                            "errorMessage": "Failed to create a new reader, the request contains invalid fields",
                            "errors":[
                            {
                            "field": "name",
                            "message": "Reader name must be longer than 5 characters, shorter than 30 characters",
                            "rejectedValue": "sd12"
                            },
                            {
                            "field": "name",
                            "message": "Reader name must contain only ENGLISH letters, spaces, dashes, apostrophes",
                            "rejectedValue": "sd12"
                            }
                            ]
                            }
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
                            "name": "Misha"
                            }
                          """)))
  @PostMapping("/readers")
  public ResponseEntity<Reader> saveReader(@Valid @RequestBody Reader reader) {
    if (reader.getId() != null) {
      throw new SaveReaderException("Request body should not contain reader id value");
    }
    var savedReader = libraryService.addNewReader(reader);
    return ResponseEntity.ok(savedReader);
  }

  @Operation(
      summary = "Get all borrowed books by reader ID",
      description = "Returns a list of all borrowed books by reader")
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
                            [
                              {
                                "id": 1,
                                "name": "1984",
                                "author": "George Orwell"
                              },
                              {
                                "id": 3,
                                "name": "Glue",
                                "author": "Irvine Welsh"
                              }
                            ]
                            """))),
    @ApiResponse(
        responseCode = "400",
        content =
            @Content(
                mediaType = "application/json",
                examples = {
                  @ExampleObject(
                      name = "Invalid ID",
                      description = "Reader ID is negative",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T19:19:33.9184707",
                            "errorMessage": "Reader ID must be a positive number"
                            }
                          """),
                  @ExampleObject(
                      name = "Book doesn't exists",
                      description = "This book ID doesn't exists in the library",
                      value =
                          """
                            {
                            "localDateTime": "2024-08-05T19:20:27.721849",
                            "errorMessage": "This Reader ID doesn't exist!"
                            }
                          """)
                }))
  })
  @GetMapping("/readers/{readerId}/books")
  public ResponseEntity<List<BookDto>> getBorrowedBooksByReaderId(
      @PathVariable("readerId")
          @NotNull
          @Positive(message = "Reader ID must be a positive number")
          @Parameter(description = "Reader ID to get books", example = "1")
          Long readerId) {
    var books = libraryService.showBorrowedBooks(readerId);
    return ResponseEntity.ok(books);
  }

  @Operation(
      summary = "Get all readers with borrowed books",
      description = "Returns list of all readers and their borrowed books in the library")
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
                                           "id": 1,
                                           "name": "Ivan",
                                           "books":[
                                           {
                                           "id": 3,
                                           "name": "Glue",
                                           "author": "Irvine Welsh"
                                           },
                                           {
                                           "id": 1,
                                           "name": "1984",
                                           "author": "George Orwell"
                                           }
                                           ]
                                           },
                                           {
                                           "id": 2,
                                           "name": "Yevhenii",
                                           "books":[
                                           {
                                           "id": 2,
                                           "name": "Home",
                                           "author": "Tony Morrison"
                                           }
                                           ]
                                           },
                                           {
                                           "id": 5,
                                           "name": "Alexey",
                                           "books":[
                                           {
                                           "id": 4,
                                           "name": "The Great Book",
                                           "author": "John Doe"
                                           }
                                           ]
                                           }
                                           ]
                                          """)))
  @GetMapping("/readers/books")
  public ResponseEntity<List<ReaderWithBooksDto>> getReadersWithBorrowedBooks() {
    var readersWithBooks = libraryService.findAllReadersWithBooks();
    return ResponseEntity.ok(readersWithBooks);
  }
}
