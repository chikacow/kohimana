package com.chikacow.kohimana.a_rate_limiting.strategy;

public abstract class RateLimitingStrategy {
    public SlidingWindowRateLimiter getSlidingWindowRateLimiter() {
        return new SlidingWindowRateLimiter();
    }

    public TokenBucketRateLimiter getTokenBucketRateLimiter() {
        return new TokenBucketRateLimiter();
    }

}
