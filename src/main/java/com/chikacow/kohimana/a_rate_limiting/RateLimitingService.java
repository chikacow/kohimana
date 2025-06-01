package com.chikacow.kohimana.a_rate_limiting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateLimitingService {

    private final RedisTemplate<String, Long> redisRateLimitingTemplate;

    public boolean isAllowed() {
        if (RateLimitConstant.getCurrentToken() > 0) {
            RateLimitConstant.useToken();
            return true;
        } else {
            return false;
        }


    }

    @Scheduled(fixedRate = 1000 * 50)
    public void resetTokenBucket() {
        RateLimitConstant.resetToken();
    }


}
