package com.chikacow.kohimana.util.enums;

public enum WorkingStatus {
    ACTIVE("ACTIVE"),
    ON_LEAVE("ON_LEAVE"),
    RESIGNED("RESIGNED"),
    TERMINATED("TERMINATED"),
    TRAINING("TRAINING"),
    SUSPENDED("SUSPENDED");

    private final String value;

    WorkingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WorkingStatus fromString(String enumString) {
        for (WorkingStatus ws : WorkingStatus.values()) {
            if (ws.value.equalsIgnoreCase(enumString)) {
                return ws;
            }
        }
        throw new IllegalArgumentException("Invalid working status: " + enumString);
    }
}

