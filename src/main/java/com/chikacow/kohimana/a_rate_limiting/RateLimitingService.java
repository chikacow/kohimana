package com.chikacow.kohimana.a_rate_limiting;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


public interface RateLimitingService {
    public boolean isAllowed();




}
