package com.chikacow.kohimana.util.helper;

import lombok.Getter;

@Getter
public enum CustomHttpStatus {
    MANUAL_EXCEPTION_HANDLED(69, "Send from exeption handler_kien");

    private final int value;


    @Getter
    private final String reasonPhrase;

    CustomHttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return value;
    }

}

