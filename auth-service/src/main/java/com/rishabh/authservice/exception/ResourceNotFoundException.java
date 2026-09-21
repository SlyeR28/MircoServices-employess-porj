package com.rishabh.authservice.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseCustomException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
