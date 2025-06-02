package com.chikacow.kohimana.a_rate_limiting.strategy;


public class SlidingWindowRateLimiter extends RateLimitingStrategy {
    @Override
    public boolean isAllowed() {
        return false;
    }
}
