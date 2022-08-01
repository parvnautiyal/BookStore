package com.spring.example.mongo.controller;

import com.spring.example.mongo.model.Books;
import com.spring.example.mongo.model.BooksDto;
import com.spring.example.mongo.service.BookStoreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookStoreController {

    private BookStoreService bookStoreService;
    private ModelMapper modelMapper;

    @Autowired
    public void setBookStoreService(BookStoreService bookStoreService) {
        this.bookStoreService = bookStoreService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<BooksDto> addBook(@RequestBody BooksDto book) {
        try {
            Books newBook = this.bookStoreService.addBook(this.modelMapper.map(book, Books.class));
            return new ResponseEntity<>(this.modelMapper.map(newBook, BooksDto.class), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<BooksDto>> getBooks() {
        try {
            List<BooksDto> allBooks = this.bookStoreService.getBooks().stream()
                    .map(books -> this.modelMapper.map(books, BooksDto.class)).toList();
            return new ResponseEntity<>(allBooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<BooksDto> getBook(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(this.modelMapper.map(bookStoreService.getBook(id), BooksDto.class),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BooksDto> updateBook(@PathVariable("id") String id, @RequestBody BooksDto book) {
        try {
            Books updatedBook = this.bookStoreService.updateBook(id, this.modelMapper.map(book, Books.class));
            return new ResponseEntity<>(this.modelMapper.map(updatedBook, BooksDto.class), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") String id) {
        try {
            this.bookStoreService.deleteBook(id);
            return new ResponseEntity<>("Book with id: " + id + " deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
