package com.library.exercise2;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.library.exercise2.service.BookService;

public class LibraryManagementApplication {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {
            BookService bookService = context.getBean(BookService.class);
            System.out.println(bookService.getBookDetails("101"));
        }
    }
}
