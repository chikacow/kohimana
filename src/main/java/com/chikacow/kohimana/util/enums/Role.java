package com.chikacow.kohimana.util.enums;

public enum Role {
    ADMIN,
    MANAGER,
    STAFF,
    CUSTOMER;


    public static String convertToString(final Role role) {
        return role.name();

    }
    public static Role fromString(String roleStr) {
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }
    }

}
