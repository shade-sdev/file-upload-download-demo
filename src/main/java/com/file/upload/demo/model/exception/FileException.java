package com.file.upload.demo.model.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {

    private final HttpStatus status;

    public FileException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
