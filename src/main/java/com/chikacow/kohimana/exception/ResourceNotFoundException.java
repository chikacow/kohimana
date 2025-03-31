package com.chikacow.kohimana.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {

        super(message);
    }
}
