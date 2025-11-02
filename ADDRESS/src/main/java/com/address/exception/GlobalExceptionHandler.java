package com.address.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends com.commomlib.exception.GlobalExceptionHandler {

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
//        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus());
//        return new ResponseEntity<>(response, ex.getStatus());
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex){
//        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus());
//        return new ResponseEntity<>(response, ex.getStatus());
//    }
//
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
//        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
//    }

}
