package com.spring.example.mongo.service;

import com.spring.example.mongo.exception.NoSuchBookException;
import com.spring.example.mongo.model.Books;
import com.spring.example.mongo.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookStoreServiceImpl implements BookStoreService {

    private static final String NOT_FOUND = "not found";
    private static final String ALREADY_EXISTS = "already exists";

    private BooksRepository booksRepository;

    @Autowired
    public void setBooksRepository(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public Books addBook(Books book) {
        if (booksRepository.findByAuthorAndName(book.getAuthor(), book.getName()).isPresent())
            throw new NoSuchBookException(getMessage(book.getId(), ALREADY_EXISTS));
        else
            return this.booksRepository.save(book);
    }

    @Override
    public List<Books> getBooks() {
        return this.booksRepository.findAll();
    }

    @Override
    public Books getBook(String id) {
        Optional<Books> book = this.booksRepository.findById(id);
        if (book.isEmpty())
            throw new NoSuchBookException(getMessage(id, NOT_FOUND));
        else
            return book.get();
    }

    @Override
    public Books updateBook(String id, Books book) {
        Optional<Books> foundBook = booksRepository.findById(id);
        if (foundBook.isEmpty()) {
            throw new NoSuchBookException(getMessage(id, ALREADY_EXISTS));
        } else {
            foundBook.get().setName(book.getName());
            foundBook.get().setInventory(book.getInventory());
            foundBook.get().setAuthor(book.getAuthor());
            foundBook.get().setPublication(book.getPublication());
            foundBook.get().setPrice(book.getPrice());
            foundBook.get().setImageUrl(book.getImageUrl());
            booksRepository.save(foundBook.get());
            return foundBook.get();
        }
    }

    @Override
    public void deleteBook(String id) {
        if (booksRepository.findById(id).isEmpty()) {
            throw new NoSuchBookException(getMessage(id, NOT_FOUND));
        } else {
            booksRepository.deleteById(id);
        }
    }

    private String getMessage(String id, String type) {
        if (type.equals(NOT_FOUND))
            return "Book with id: " + id + " not found!";
        else
            return "Book with id: " + id + " already exists!";
    }

}
