package com.example.booklibrary.service;

import com.example.booklibrary.dao.BookDao;
import com.example.booklibrary.dao.ReaderDao;
import com.example.booklibrary.entity.Book;
import com.example.booklibrary.entity.Reader;
import com.example.booklibrary.exception.LibraryServiceException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {
  private static final String BOOK_NOT_FOUND = "This Book ID doesn't exist!";
  private static final String READER_NOT_FOUND = "This Reader ID doesn't exist!";
  private final BookDao bookDao;
  private final ReaderDao readerDao;

  public LibraryService(BookDao bookDao, ReaderDao readerDao) {
    this.bookDao = bookDao;
    this.readerDao = readerDao;
  }

  public List<Book> findAllBooks() {
    return bookDao.findAll();
  }

  public List<Reader> findAllReader() {
    return readerDao.findAll();
  }

  public Optional<Reader> showCurrentReaderOfBook(Long bookId) {
    bookDao.findById(bookId).orElseThrow(() -> new LibraryServiceException(BOOK_NOT_FOUND));

    return readerDao.findReaderByBookId(bookId);
  }

  public List<Book> showBorrowedBooks(Long readerId) {
    readerDao.findById(readerId).orElseThrow(() -> new LibraryServiceException(READER_NOT_FOUND));

    return bookDao.findAllByReaderId(readerId);
  }

  public Reader addNewReader(Reader reader) {
    return readerDao.save(reader);
  }

  public Book addNewBook(Book book) {
    return bookDao.save(book);
  }

  public void borrowBook(Long bookId, Long readerId) {
    bookDao.findById(bookId).orElseThrow(() -> new LibraryServiceException(BOOK_NOT_FOUND));
    readerDao.findById(readerId).orElseThrow(() -> new LibraryServiceException(READER_NOT_FOUND));
    readerDao
        .findReaderByBookId(bookId)
        .ifPresent(
            reader -> {
              throw new LibraryServiceException("Cannot borrow already borrowed Book!");
            });

    bookDao.borrow(bookId, readerId);
  }

  public void returnBookToLibrary(Long bookId) {
    bookDao.findById(bookId).orElseThrow(() -> new LibraryServiceException(BOOK_NOT_FOUND));
    readerDao
        .findReaderByBookId(bookId)
        .orElseThrow(
            () ->
                new LibraryServiceException("Cannot return Book. Book is already in the Library!"));
    bookDao.returnBook(bookId);
  }

  public Map<Reader, List<Book>> findAllReadersWithBooks() {
    return readerDao.findAllWithBooks();
  }

  public Map<Book, Optional<Reader>> findAllBooksWithReaders() {
    return bookDao.findAllWithReaders();
  }
}
