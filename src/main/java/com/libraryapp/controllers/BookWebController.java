package com.libraryapp.controllers;

import com.libraryapp.entities.Book;
import com.libraryapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookWebController {
    @Autowired
    BookService bookService;

    @GetMapping("/bookweb")
    public Book book(@RequestParam(value = "name",defaultValue = "World") String name){
        BookService bookService = new BookService();
        bookService.findAll();

        return (Book) bookService.findAll();

    }



}
