package com.chikacow.kohimana.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    FOOD("food"),
    DRINK("drink"),
    OTHER("other");

    private final String value;

}
