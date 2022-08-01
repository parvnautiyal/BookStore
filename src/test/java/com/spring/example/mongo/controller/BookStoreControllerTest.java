package com.spring.example.mongo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.example.mongo.model.Books;
import com.spring.example.mongo.model.BooksDto;
import com.spring.example.mongo.service.BookStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class BookStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private BookStoreService bookStoreService;

    private BooksDto book1;
    private BooksDto book2;

    @BeforeEach
    void setup() {
        book1 = BooksDto.builder().id("Book1").name("Test Book").inventory(22).author("Test Author")
                .publication("Test Publisher").price(29).imageUrl("www.test.com/testImage").build();

        book2 = BooksDto.builder().id("Book1").name("Updated Test Book").inventory(55).author("Updated Test Author")
                .publication("Updated Test Publisher").price(49).imageUrl("www.test.com/testImage/updated").build();
    }

    @Test
    @DisplayName("Testcase to test controller to add book")
    void addBookTest() throws Exception {
        given(bookStoreService.addBook(any(Books.class))).willReturn(modelMapper.map(book1, Books.class));

        System.out.println("String->"+bookStoreService.addBook(any(Books.class)));

        ResultActions response = mockMvc.perform(post("/api/books/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelMapper.map(book1, BooksDto.class))));

        response.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(book1.getName())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor())));

    }

    @Test
    @DisplayName("Testcase to test controller to get all books")
    void getBooksTest() throws Exception {
        given(bookStoreService.getBooks())
                .willReturn(Stream.of(book1, book2).map(booksDto -> modelMapper.map(booksDto, Books.class)).toList());

        ResultActions response = mockMvc.perform(get("/api/books/all"));

        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("Testcase to test controller to get book by id")
    void getBookTest() throws Exception {
        given(bookStoreService.getBook(any(String.class))).willReturn(modelMapper.map(book1, Books.class));

        ResultActions response = mockMvc.perform(get("/api/books/{id}", book1.getId()));

        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.name", is(book1.getName())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor())));
    }

    @Test
    @DisplayName("Testcase to test controller to get book by id which is not present")
    void getBookTestException() throws Exception {
        given(bookStoreService.getBook((any(String.class)))).willReturn(null);

        ResultActions response = mockMvc.perform(get("/api/books/{id}", book1.getId()));

        response.andExpect(status().isNotFound()).andDo(print());

    }

    @Test
    @DisplayName("Testcase to test controller to update book by id")
    void updateTest() throws Exception {
        given(bookStoreService.updateBook(any(String.class), any(Books.class)))
                .willReturn(modelMapper.map(book2, Books.class));

        ResultActions response = mockMvc.perform(put("/api/books/book/{id}", book1.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book2)));

        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.name", is(book2.getName())))
                .andExpect(jsonPath("$.author", is(book2.getAuthor())));
    }

    @Test
    @DisplayName("Testcase to test controller to update book by id when book not found")
    void updateTestException() throws Exception {
        given(bookStoreService.updateBook(any(String.class), any(Books.class))).willReturn(null);

        ResultActions response = mockMvc.perform(put("/api/books/book/{id}", book1.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book2)));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @DisplayName("Testcase to test controller to delete book by id")
    void deleteTest() throws Exception {
        willDoNothing().given(bookStoreService).deleteBook(any(String.class));

        ResultActions response = mockMvc.perform(delete("/api/books/book/{id}", book1.getId()));

        response.andExpect(status().isOk()).andDo(print());
    }
}
