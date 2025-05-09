package com.chikacow.kohimana.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {
    ACTIVE(1, "active"),
    INACTIVE(0, "inactive");

    private final int code;
    private final String description;

    // Optional: Helper to get enum by code
    public static AccountStatus fromCode(int code) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

}
