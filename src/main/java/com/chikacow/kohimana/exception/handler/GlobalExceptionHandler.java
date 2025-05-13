package com.chikacow.kohimana.exception.handler;


import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.exception.SaveToDBException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> getExceptionHandler(Exception e) {

        return new ResponseEntity<>(e.getMessage() + " " + e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SaveToDBException.class)
    public ResponseEntity<String> handleSaveDBExeption (Exception e) {

        return new ResponseEntity<>(e.getMessage() + " " + e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException (Exception e) {

        return new ResponseEntity<>(e.getMessage() + " " + e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
