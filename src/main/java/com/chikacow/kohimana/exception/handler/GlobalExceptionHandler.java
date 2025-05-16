package com.chikacow.kohimana.exception.handler;


import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.dto.response.ResponseException;
import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.exception.SaveToDBException;
import org.simpleframework.xml.transform.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseException<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        String exceptionCause = (cause == null) ? null : cause.getClass().getName();

        Map<String, String> response = new HashMap<>();

        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            response.put("error", ex.getMessage());
        } else {
            response.put("error", "Invalid request. Could not read JSON.");
        }

        int status = HttpStatus.BAD_REQUEST.value();

        return ResponseEntity.status(status).body(ResponseException.builder()
                .status(status)
                .message("Deserialization format layer problem")
                .exCause(exceptionCause)
                .exClass(ex.getClass().getName())
                .data(response)
                .build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseException<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Throwable cause = ex.getCause();
        String exceptionCause = (cause == null) ? null : cause.getClass().getName();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        int status = HttpStatus.BAD_REQUEST.value();

        return ResponseEntity.status(status).body(ResponseException.builder()
                .status(status)
                .message("Invalid username format")
                .exCause(exceptionCause)
                .exClass(ex.getClass().getName())
                .data(errors)
                .build());
    }




    @ExceptionHandler(SaveToDBException.class)
    public ResponseEntity<ResponseException<?>> handleSaveDBExeption (SaveToDBException ex) {

        Throwable cause = ex.getCause();
        String exceptionCause = (cause == null) ? null : cause.getClass().getName();
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getLocalizedMessage());

        int status = HttpStatus.BAD_REQUEST.value();

        return ResponseEntity.status(status).body(ResponseException.builder()
                .status(status)
                .message("Error saving to database")
                .exCause(exceptionCause)
                .exClass(ex.getClass().getName())
                .data(error)
                .build());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ResponseException<?>> handleInvalidDataException (InvalidDataException ex) {
        Throwable cause = ex.getCause();
        String exceptionCause = (cause == null) ? null : cause.getClass().getName();
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getLocalizedMessage());
        int status = HttpStatus.BAD_REQUEST.value();

        return ResponseEntity.status(status).body(ResponseException.builder()
                .status(status)
                .message("Invalid input")
                .exCause(exceptionCause)
                .exClass(ex.getClass().getName())
                .data(error)
                .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        Throwable cause = ex.getCause();
        String exceptionCause = (cause == null) ? null : cause.getClass().getName();
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getLocalizedMessage());

        int status = HttpStatus.BAD_REQUEST.value();

        return ResponseEntity.status(status).body(ResponseException.builder()
                .status(status)
                .message("Path variable data type mismatch")
                .exCause(exceptionCause)
                .exClass(ex.getClass().getName())
                .data(error)
                .build());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseException<?>> getExceptionHandler(Exception ex) {
        Throwable cause = ex.getCause();
        String exceptionCause = (cause == null) ? null : cause.getClass().getName();
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        int status = HttpStatus.BAD_REQUEST.value();

        return ResponseEntity.status(status).body(ResponseException.builder()
                .status(status)
                .message("Unknown error")
                .exCause(exceptionCause)
                .exClass(ex.getClass().getName())
                .data(error)
                .build());

    }
}
