package com.rishabh.authservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseCustomException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
