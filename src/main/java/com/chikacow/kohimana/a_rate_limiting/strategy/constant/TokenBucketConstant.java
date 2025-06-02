package com.chikacow.kohimana.a_rate_limiting.strategy.constant;

import com.chikacow.kohimana.service.HealthMonitoringService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Slf4j
public class TokenBucketConstant {


    private static boolean redisStatus;

    @Getter
    private static final Long MAX_TOKEN = 5L;

    @Getter
    private static final String REDIS_TOKEN_BUCKET_KEY = "tokenBucketKey";

    @Getter
    private static final AtomicLong currentToken = new AtomicLong(MAX_TOKEN);

    public static void init(HealthMonitoringService healthMonitoringService) {
        redisStatus = healthMonitoringService.isRedisUp();

        if (redisStatus) {
            log.info("Redis connected successfully");
        } else {
            log.info("Redis connected failed");
        }

        currentToken.set(MAX_TOKEN);
    }
    public static void resetToken() {
        currentToken.set(MAX_TOKEN);
    }

    public static void useToken() {
        currentToken.decrementAndGet();
    }

    public static boolean getRedisStatus() {
        return redisStatus;
    }


}
