package com.chikacow.kohimana.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }

}
