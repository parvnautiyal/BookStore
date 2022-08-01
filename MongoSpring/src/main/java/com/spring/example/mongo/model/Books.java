package com.spring.example.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "Books")
public class Books {
    @Id
    private String id;
    private String name;
    private long inventory;
    private String author;
    private String publication;
    private long price;
    private String imageUrl;
}
