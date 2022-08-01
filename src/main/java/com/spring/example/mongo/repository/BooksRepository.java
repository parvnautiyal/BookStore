package com.spring.example.mongo.repository;

import com.spring.example.mongo.model.Books;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksRepository extends MongoRepository<Books, String> {
    Optional<Books> findByAuthorAndName(String author, String name);
}
