package com.address.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {

    private String message;
    private HttpStatus status;

    public BadRequestException(String message) {
//        super(message);
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
