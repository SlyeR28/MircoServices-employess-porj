package com.rishabh.authservice.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends BaseCustomException {
    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
