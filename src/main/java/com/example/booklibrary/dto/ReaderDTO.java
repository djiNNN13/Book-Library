package com.example.booklibrary.dto;

import com.example.booklibrary.entity.Book;

import java.util.List;

public class ReaderDTO {
    private Long id;
    private String name;
    private List<Book> books;

    public ReaderDTO(Long id, String name, List<Book> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
