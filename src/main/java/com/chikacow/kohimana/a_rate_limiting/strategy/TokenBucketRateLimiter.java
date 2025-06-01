package com.chikacow.kohimana.a_rate_limiting.strategy;

import com.chikacow.kohimana.a_rate_limiting.RateLimitingService;
import com.chikacow.kohimana.a_rate_limiting.TokenBucketConstant;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("token-bucket")
@RequiredArgsConstructor
public class TokenBucketRateLimiter extends RateLimitingStrategy implements RateLimitingService {
    private final RedisTemplate<String, Long> redisRateLimitingTemplate;

    @PostConstruct
    public void init() {
        if (redisRateLimitingTemplate.opsForValue().get(TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY()) == null) {
            redisRateLimitingTemplate.opsForValue().set(TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY(), TokenBucketConstant.getMAX_TOKEN());
        }
    }

    @Override
    public boolean isAllowed() {
        if (getTokenFromRedis() > 0L) {
            TokenBucketConstant.useToken();
            setTokenToRedis();
            return true;
        } else {
            return false;
        }


    }

    @Scheduled(fixedRate = 1000 * 50)
    public void resetTokenBucket() {
        TokenBucketConstant.resetToken();
        setTokenToRedis();
    }

    private Long getTokenFromRedis() {
        Long token = redisRateLimitingTemplate.opsForValue().get(TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY());
        return token != null ? token : 0L;

    }

    private void setTokenToRedis() {
        redisRateLimitingTemplate.opsForValue().set(TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY(), TokenBucketConstant.getCurrentToken());

    }


}
