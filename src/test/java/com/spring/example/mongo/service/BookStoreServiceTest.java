package com.spring.example.mongo.service;

import com.spring.example.mongo.exception.NoSuchBookException;
import com.spring.example.mongo.model.Books;
import com.spring.example.mongo.repository.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookStoreServiceTest {

    @Mock
    private BooksRepository booksRepository;
    @InjectMocks
    private BookStoreServiceImpl bookStoreService;
    private Books book1;
    private Books book2;

    @BeforeEach
    public void setup() {
        book1 = Books.builder().id("Book 1").name("Test Book").inventory(22).author("Test Author")
                .publication("Test Publisher").price(29).imageUrl("www.test.com/testImage").build();

        book2 = Books.builder().id("Book 1").name("Updated Test Book").inventory(55).author("Updated Test Author")
                .publication("Updated Test Publisher").price(49).imageUrl("www.test.com/testImage/updated").build();
    }

    @Test
    @DisplayName("Testcase for save book operation")
    void saveTest() {
        given(booksRepository.findByAuthorAndName(book1.getAuthor(), book1.getName())).willReturn(Optional.empty());
        given(booksRepository.save(book1)).willReturn(book1);

        Books savedBook = bookStoreService.addBook(book1);

        assertThat(savedBook).isNotNull().isEqualTo(book1);
    }

    @Test
    @DisplayName("Testcase for save book operation with exception")
    void saveTestException() {
        given(booksRepository.findByAuthorAndName(book1.getAuthor(), book1.getName())).willReturn(Optional.of(book1));

        assertThrows(NoSuchBookException.class, () -> {
            bookStoreService.addBook(book1);
        });

        verify(booksRepository, never()).save(any(Books.class));
    }

    @Test
    @DisplayName("Testcase for update book operation")
    void updateTest() {
        given(booksRepository.findById(book1.getId())).willReturn(Optional.ofNullable(book1));

        Books updatedBook = bookStoreService.updateBook(book1.getId(), book2);

        assertThat(updatedBook).isNotNull().hasToString(book2.toString());
    }

    @Test
    @DisplayName("Testcase for update book operation with exception")
    void updateTestException() {
        given(booksRepository.findById(book1.getId())).willReturn(Optional.empty());

        assertThrows(NoSuchBookException.class, () -> {
            bookStoreService.updateBook("Book 1", book2);
        });

        verify(booksRepository, never()).save(any(Books.class));
    }

    @Test
    @DisplayName("Testcase for get all books operation")
    void getAllTest() {
        given(booksRepository.findAll()).willReturn(List.of(book1, book2));

        List<Books> books = bookStoreService.getBooks();

        assertThat(books).isNotNull().hasSize(2).containsExactlyInAnyOrderElementsOf(List.of(book1, book2));
    }

    @Test
    @DisplayName("Testcase for get all books operation on empty database")
    void getAllTestException() {
        given(booksRepository.findAll()).willReturn(Collections.emptyList());

        List<Books> books = bookStoreService.getBooks();

        assertThat(books).isEmpty();
    }

    @Test
    @DisplayName("Testcase for get books by id operation")
    void getByIdTest() {
        given(booksRepository.findById(book1.getId())).willReturn(Optional.of(book1));

        Books foundBook = bookStoreService.getBook(book1.getId());

        assertThat(foundBook).isNotNull().isEqualTo(book1);
    }

    @Test
    @DisplayName("Testcase for get books by id operation with exception")
    void getByIdTestException() {
        given(booksRepository.findById(book1.getId())).willReturn(Optional.empty());

        assertThrows(NoSuchBookException.class, () -> {
            bookStoreService.getBook("Book 1");
        });
    }

    @Test
    @DisplayName("Testcase for delete book by id operation")
    void deleteTest() {
        given(booksRepository.findById("Book 1")).willReturn(Optional.of(book1));

        willDoNothing().given(booksRepository).deleteById("Book 1");

        bookStoreService.deleteBook("Book 1");

        verify(booksRepository, times(1)).deleteById("Book 1");
    }

    @Test
    @DisplayName("Testcase for delete book by id operation with exception")
    void deleteTestException() {
        given(booksRepository.findById("Book 1")).willReturn(Optional.empty());

        assertThrows(NoSuchBookException.class, () -> {
            bookStoreService.deleteBook("Book 1");
        });

        verify(booksRepository, times(0)).deleteById("Book 1");
    }
}
