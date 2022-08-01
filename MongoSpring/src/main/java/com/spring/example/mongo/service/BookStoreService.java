package com.spring.example.mongo.service;

import com.spring.example.mongo.model.Books;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookStoreService {

    Books addBook(Books book);

    List<Books> getBooks();

    Books getBook(String id);

    Books updateBook(String id, Books book);

    void deleteBook(String id);
}
