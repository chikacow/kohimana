package com.chikacow.kohimana.a_rate_limiting;

import lombok.Getter;


public class TokenBucketConstant {
    @Getter
    private static final Long MAX_TOKEN = 5L;

    @Getter
    private static final String REDIS_TOKEN_BUCKET_KEY = "tokenBucketKey";

    @Getter
    private static Long currentToken;

    public TokenBucketConstant() {
        currentToken = MAX_TOKEN;
    }
    public static void resetToken() {
        currentToken = MAX_TOKEN;
    }

    public static void useToken() {
        currentToken -= 1;
    }


}
