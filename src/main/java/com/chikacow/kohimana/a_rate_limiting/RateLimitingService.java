package com.chikacow.kohimana.a_rate_limiting;


import com.chikacow.kohimana.a_rate_limiting.strategy.TokenBucketRateLimiter;

public interface RateLimitingService {

    public boolean isAllowed();




}
