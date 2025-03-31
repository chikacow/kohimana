package com.chikacow.kohimana.exception;


import com.chikacow.kohimana.util.helper.CustomHttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> getExceptionHandler(Exception e) {
        //return ResponseEntity.status(69).body(e.getMessage() + " " + e.getClass());
        return new ResponseEntity<>(e.getMessage() + " " + e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
