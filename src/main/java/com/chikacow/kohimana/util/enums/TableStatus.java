package com.chikacow.kohimana.util.enums;

public enum TableStatus {
    AVAILABLE("AVAILABLE"), OCCUPIED("OCCUPIED"), RESERVED("RESERVED"), CLEANING("CLEANING");

    private final String value;

    TableStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TableStatus fromString(String enumString) {
        for (TableStatus ts : TableStatus.values()) {
            if (ts.value.equalsIgnoreCase(enumString)) {
                return ts;
            }
        }
        throw new IllegalArgumentException("Invalid table status: " + enumString);
    }
}