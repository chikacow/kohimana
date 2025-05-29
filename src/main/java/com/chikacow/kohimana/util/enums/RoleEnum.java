package com.chikacow.kohimana.util.enums;

public enum RoleEnum {
    ADMIN,
    MANAGER,
    STAFF,
    CUSTOMER;


    public static String convertToString(final RoleEnum role) {
        return role.name();

    }
    public static RoleEnum fromString(String roleStr) {
        try {
            return RoleEnum.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }
    }

}
