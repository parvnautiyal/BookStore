package com.spring.example.mongo.repository;

import com.spring.example.mongo.configuration.MongoConfiguration;
import com.spring.example.mongo.model.Books;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static com.spring.example.mongo.configuration.MongoConfiguration.configureAndStartEmbeddedMongoDB;
import static com.spring.example.mongo.configuration.MongoConfiguration.stopAll;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@ContextConfiguration(classes = { MongoConfiguration.class })
public class BooksRepositoryTest {

    private BooksRepository booksRepository;

    @Autowired
    public void setBooksRepository(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    private Books book1;

    @BeforeAll
    public static void generalSetup() throws IOException {
        configureAndStartEmbeddedMongoDB();
    }

    @BeforeEach
    public void dataSetup() {
        book1 = Books.builder().id("Book 1").name("Test Book").inventory(22).author("Test Author")
                .publication("Test Publisher").price(29).imageUrl("www.test.com/testImage").build();
    }

    @Test
    @DisplayName("Testcase to verify findAll operation")
    void getTest() {
        booksRepository.save(book1);
        assertThat(booksRepository.findAll()).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("Testcase to verify save operation")
    void addTest() {
        assertThat(booksRepository.save(book1)).isNotNull().isEqualTo(book1);
    }

    @Test
    @DisplayName("Testcase to verify findById operation")
    void getByIdTest() {
        booksRepository.save(book1);
        booksRepository.findById("Book 1").ifPresent(books -> {
            assertThat(books).isNotNull();
            assertThat(books.getId()).isEqualTo("Book 1");
        });
    }

    @Test
    @DisplayName("Testcase to verify delete operation")
    void deleteByTest() {
        booksRepository.save(book1);
        booksRepository.deleteById("Book 1");
        assertThat(booksRepository.findById("Book 1")).isEmpty();
    }

    @AfterAll
    public static void tearDown() {
        stopAll();
    }

}
