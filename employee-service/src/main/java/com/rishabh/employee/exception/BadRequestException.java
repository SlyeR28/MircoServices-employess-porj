package com.rishabh.employee.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends RuntimeException{

    private String message;
    private HttpStatus httpStatus;

    public BadRequestException(String message) {
//         super(message);
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BadRequestException(String message, HttpStatus httpStatus) {
//        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
