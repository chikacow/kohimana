package com.chikacow.kohimana.exception;

public class HaveNoAccessToResourceException extends RuntimeException {
    public HaveNoAccessToResourceException(String message) {
        super(message);
    }
}
