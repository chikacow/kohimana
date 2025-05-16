package com.chikacow.kohimana.dto.response;


import lombok.*;
import org.checkerframework.checker.units.qual.A;

public class ResponseError<T> extends ResponseData<T> {
    public ResponseError(int status, String message, T data) {
        super(status, message, data);
    }
}

