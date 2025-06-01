package com.chikacow.kohimana.a_rate_limiting;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class RateLimitingService {

    private final RedisTemplate<String, Long> redisRateLimitingTemplate;

    public RateLimitingService(RedisTemplate<String, Long> redisRateLimitingTemplate) {

        this.redisRateLimitingTemplate = redisRateLimitingTemplate;

    }
    @PostConstruct
    public void init() {
        if (redisRateLimitingTemplate.opsForValue().get(RateLimitConstant.getREDIS_TOKEN_BUCKET_KEY()) == null) {
            redisRateLimitingTemplate.opsForValue().set(RateLimitConstant.getREDIS_TOKEN_BUCKET_KEY(), RateLimitConstant.getMAX_TOKEN());
        }
    }

    public boolean isAllowed() {
        if (getTokenFromRedis() > 0L) {
            RateLimitConstant.useToken();
            setTokenToRedis();
            return true;
        } else {
            return false;
        }


    }

    @Scheduled(fixedRate = 1000 * 50)
    public void resetTokenBucket() {
        RateLimitConstant.resetToken();
        setTokenToRedis();
    }

    private Long getTokenFromRedis() {
        Long token = redisRateLimitingTemplate.opsForValue().get(RateLimitConstant.getREDIS_TOKEN_BUCKET_KEY());
        return token != null ? token : 0L;

    }

    private void setTokenToRedis() {
        redisRateLimitingTemplate.opsForValue().set(RateLimitConstant.getREDIS_TOKEN_BUCKET_KEY(), RateLimitConstant.getCurrentToken());

    }

}
