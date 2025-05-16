package com.chikacow.kohimana.util.enums;

public enum Gender {

    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender fromString(String enumString) {
        for (Gender gender : Gender.values()) {
            if (gender.value.equalsIgnoreCase(enumString)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender: " + enumString);
    }
}
