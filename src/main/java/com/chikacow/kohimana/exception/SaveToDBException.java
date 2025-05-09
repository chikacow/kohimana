package com.chikacow.kohimana.exception;

public class SaveToDBException extends RuntimeException {
    public SaveToDBException(String message) {
        super(message);
    }
}
