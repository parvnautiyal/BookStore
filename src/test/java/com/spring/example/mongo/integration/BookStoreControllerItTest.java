package com.spring.example.mongo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.example.mongo.model.Books;
import com.spring.example.mongo.model.BooksDto;
import com.spring.example.mongo.repository.BooksRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.List;

import static com.spring.example.mongo.configuration.MongoConfiguration.configureAndStartEmbeddedMongoDB;
import static com.spring.example.mongo.configuration.MongoConfiguration.stopAll;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookStoreControllerItTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private ModelMapper modelMapper;

    private BooksDto book1;
    private BooksDto book2;

    @BeforeAll
    public static void cleanUp() throws IOException {
        configureAndStartEmbeddedMongoDB();
    }

    @BeforeEach
    void setup() {
        book1 = BooksDto.builder().id("Book 1").name("Test Book").inventory(22).author("Test Author")
                .publication("Test Publisher").price(29).imageUrl("www.test.com/testImage").build();

        book2 = BooksDto.builder().id("Book1").name("Updated Test Book").inventory(55).author("Updated Test Author")
                .publication("Updated Test Publisher").price(49).imageUrl("www.test.com/testImage/updated").build();

        booksRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration test for controller to add book")
    void addBookTest() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/books/add").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelMapper.map(book1, BooksDto.class))));

        response.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(book1.getName())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor()))).andDo(print());

    }

    @Test
    @DisplayName("Integration test for controller to get all books")
    void getBooksTest() throws Exception {

        booksRepository.saveAll(List.of(modelMapper.map(book1, Books.class), modelMapper.map(book2, Books.class)));

        ResultActions response = mockMvc.perform(get("/api/books/all"));

        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("Integration test for controller to get book by id")
    void getBookTest() throws Exception {
        booksRepository.save(modelMapper.map(book1, Books.class));

        ResultActions response = mockMvc.perform(get("/api/books/{id}", book1.getId()));

        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.name", is(book1.getName())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor())));
    }

    @Test
    @DisplayName("Integration test for controller to get book by id when book not present")
    void getBookTestException() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/books/{id}", book1.getId()));

        response.andExpect(status().isNotFound()).andDo(print());

    }

    @Test
    @DisplayName("Integration test for controller to update book by id")
    void updateTest() throws Exception {
        booksRepository.save(modelMapper.map(book1, Books.class));

        ResultActions response = mockMvc.perform(put("/api/books/book/{id}", book1.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book2)));

        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.name", is(book2.getName())))
                .andExpect(jsonPath("$.author", is(book2.getAuthor())));
    }

    @Test
    @DisplayName("Integration test for controller to update book by id when book not present")
    void updateTestException() throws Exception {
        ResultActions response = mockMvc.perform(put("/api/books/book/{id}", book1.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(book2)));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @DisplayName("Integration test for controller to delete book by id")
    void deleteTest() throws Exception {
        booksRepository.save(modelMapper.map(book1, Books.class));

        ResultActions response = mockMvc.perform(delete("/api/books/book/{id}", book1.getId()));

        response.andExpect(status().isOk()).andDo(print());
    }

    @AfterAll
    public static void tearDown() {
        stopAll();
    }
}
