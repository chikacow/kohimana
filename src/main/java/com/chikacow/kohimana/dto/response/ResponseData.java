package com.chikacow.kohimana.dto.response;


import lombok.RequiredArgsConstructor;

//just the data, should be included in a higher response entity

@RequiredArgsConstructor
public class ResponseData<T> {
    private final int status;
    private final String message;
    private T data;


}
