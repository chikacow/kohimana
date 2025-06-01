package com.chikacow.kohimana.a_rate_limiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


public class RateLimitConstant {
    @Getter
    private static final Long MAX_TOKEN = 8L;

    @Getter
    private static Long currentToken;

    public RateLimitConstant() {
        currentToken = MAX_TOKEN;
    }
    public static void resetToken() {
        currentToken = MAX_TOKEN;
    }

    public static void useToken() {
        currentToken -= 1;
    }


}
