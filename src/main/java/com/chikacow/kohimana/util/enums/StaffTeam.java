package com.chikacow.kohimana.util.enums;

public enum StaffTeam {
    BARISTA("BARISTA"),
    CASHIER("CASHIER"),
    SERVER("SERVER"),
    CLEANING("CLEANING"),
    KITCHEN("KITCHEN"),
    MANAGER("MANAGER"),
    INVENTORY("INVENTORY"),
    SECURITY("SECURITY"),
    SUPPORT("SUPPORT");

    private final String value;

    StaffTeam(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StaffTeam fromString(String enumString) {
        for (StaffTeam st : StaffTeam.values()) {
            if (st.value.equalsIgnoreCase(enumString)) {
                return st;
            }
        }
        throw new IllegalArgumentException("Invalid staff team: " + enumString);
    }
}