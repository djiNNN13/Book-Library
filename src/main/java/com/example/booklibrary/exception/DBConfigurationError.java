package com.example.booklibrary.exception;

public class DBConfigurationError extends RuntimeException{
    public DBConfigurationError(String message, Throwable cause) {
        super(message, cause);
    }
}
