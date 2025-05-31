package com.chikacow.kohimana.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum CategoryType {
    FOOD("food"),
    DRINK("drink"),
    OTHER("other");

    private final String value;



    CategoryType(String value) {
        this.value = value;
    }

    public static Object fromString(StaffTeam staffTeam, Object setStaffTeam) {
        return null;
    }

    public String getValue() {
        return value;
    }

    public static CategoryType fromString(String enumString) {
        for (CategoryType cate : CategoryType.values()) {
            if (cate.value.equalsIgnoreCase(enumString)) {
                return cate;
            }
        }
        throw new IllegalArgumentException("Invalid cate: " + enumString);
    }

}
