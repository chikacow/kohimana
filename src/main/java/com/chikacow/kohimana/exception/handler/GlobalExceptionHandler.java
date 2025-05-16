package com.chikacow.kohimana.exception.handler;


import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.exception.SaveToDBException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> getExceptionHandler(Exception e) {

        return new ResponseEntity<>(e.getMessage() + "" + e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
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
