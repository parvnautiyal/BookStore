package com.spring.example.mongo.exception;

public class NoSuchBookException extends RuntimeException {

    public NoSuchBookException(String msg) {
        super(msg);
    }
}
