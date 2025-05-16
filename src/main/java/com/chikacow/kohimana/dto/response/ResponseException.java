package com.chikacow.kohimana.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
@Getter
public class ResponseException<T> {
    private final int status;
    private final String message;
    private final String exClass;
    private final String exCause;
    private final T data;
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));



}