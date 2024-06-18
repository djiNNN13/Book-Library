package com.example.booklibrary.exception;

public class BookNotBorrowedException extends RuntimeException{
    public BookNotBorrowedException(String message) {
        super(message);
    }
}
