package com.rishabh.employee.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class CustomException extends RuntimeException{

    @Getter
    private HttpStatus statusCode;
    public CustomException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CustomException(String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
