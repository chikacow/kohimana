package com.chikacow.kohimana.a_rate_limiting.strategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenBucketRateLimiter extends RateLimitingStrategy {
    private final long maxTokens = 10;


}
