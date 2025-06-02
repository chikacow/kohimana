package com.chikacow.kohimana.a_rate_limiting.strategy;

import com.chikacow.kohimana.a_rate_limiting.RateLimitingService;
import com.chikacow.kohimana.a_rate_limiting.strategy.constant.TokenBucketConstant;
import com.chikacow.kohimana.controller.HealthMonitoring;
import com.chikacow.kohimana.service.HealthMonitoringService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service("token-bucket")
@RequiredArgsConstructor
public class TokenBucketRateLimiter extends RateLimitingStrategy implements RateLimitingService {

    private final RedisTemplate<String, Long> redisRateLimitingTemplate;
    private final HealthMonitoringService healthMonitoringService;

    @PostConstruct
    public void init() {

        TokenBucketConstant.init(healthMonitoringService);
        if (TokenBucketConstant.getRedisStatus()) {

            if (redisRateLimitingTemplate.opsForValue().get(TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY()) != null) {
                redisRateLimitingTemplate.delete(TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY());
            }
            redisRateLimitingTemplate.opsForValue()
                    .set(
                            TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY(),
                            TokenBucketConstant.getMAX_TOKEN(),
                            Duration.ofSeconds(24 * 3600)
                    );
        } else {
            TokenBucketConstant.resetToken();
        }
    }

    @Override
    public boolean isAllowed() {
        if (TokenBucketConstant.getRedisStatus()) {
            if (getTokenFromRedis() > 0L) {
                TokenBucketConstant.useToken();
                setTokenToRedis();
                return true;
            } else {
                return false;
            }
        } else {

            if (TokenBucketConstant.getCurrentToken().get() > 0L) {
                TokenBucketConstant.useToken();
                return true;
            } else {
                return false;
            }

        }

    }

    @Scheduled(fixedRate = 1000*50)
    public void resetTokenBucket() {
        if (TokenBucketConstant.getRedisStatus()) {
            TokenBucketConstant.resetToken();
            setTokenToRedis();
        } else {
            TokenBucketConstant.resetToken();
        }
    }

    /**
     * Handle case when redis suddenly die -> the server should switch to local processing smoothly
     */
    //@Scheduled(fixedRate = 100)
    public void syncRedisConnection() {

    }

    private Long getTokenFromRedis() {
        Long token = redisRateLimitingTemplate.opsForValue().get(TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY());
        return token != null ? token : 0L;

    }

    private void setTokenToRedis() {
        redisRateLimitingTemplate.opsForValue()
                .set(
                        TokenBucketConstant.getREDIS_TOKEN_BUCKET_KEY(),
                        TokenBucketConstant.getCurrentToken().get(),
                        Duration.ofSeconds(24*3600)
                );

    }


}
