package com.spring.example.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.example.mongo.controller.BookStoreController;
import com.spring.example.mongo.repository.BooksRepository;
import com.spring.example.mongo.service.BookStoreService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MongoAppApplicationTests {

    @Autowired
    private BookStoreController bookStoreController;

    @Autowired
    private BookStoreService bookStoreService;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void contextLoads() {
        assertThat(bookStoreController).isNotNull();
        assertThat(bookStoreService).isNotNull();
        assertThat(booksRepository).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(modelMapper).isNotNull();
    }

}
