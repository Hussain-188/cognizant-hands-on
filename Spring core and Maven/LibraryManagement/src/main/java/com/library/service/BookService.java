package com.library.service;

import com.library.repository.BookRepository;

public class BookService {

    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public String getBookDetails(String bookId) {
        if (bookRepository == null) {
            throw new IllegalStateException("BookRepository has not been configured");
        }
        return bookRepository.findBookById(bookId);
    }
}