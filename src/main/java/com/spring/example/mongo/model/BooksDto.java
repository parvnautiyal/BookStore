package com.spring.example.mongo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BooksDto {
    private String id;
    private String name;
    private long inventory;
    private String author;
    private String publication;
    private long price;
    private String imageUrl;
}
