package com.library.repository;

public class BookRepository {

    public String findBookById(String bookId) {
        return "Book[id=" + bookId + ", title=Spring in Action, author=Craig Walls]";
    }
}