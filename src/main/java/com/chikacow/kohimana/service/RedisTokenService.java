package com.chikacow.kohimana.service;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.model.redis.RedisToken;
import com.chikacow.kohimana.repository.redis.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "REDIS-SERVICE")
public class RedisTokenService {
    private final RedisTokenRepository redisTokenRepository;

    public String save(RedisToken redisToken) {
        RedisToken save = redisTokenRepository.save(redisToken);
        return save.getId();
    }

    public void delete(String id) {
        try {
            redisTokenRepository.deleteById(id);
        } catch (Exception e) {
            log.info("Redis not connected");
        }
    }

    public RedisToken getById(String id) {
        try {
            return redisTokenRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Token not fount in redis"));
        } catch (Exception e) {
            log.info("Redis not connected");
        }
    }

    public void save(Token userAuthenticationToken) {
        //save to redis as well
        try {
            this.save(RedisToken.builder()
                    .id(userAuthenticationToken.getUsername())
                    .accessToken(userAuthenticationToken.getAccessToken())
                    .refreshToken(userAuthenticationToken.getRefreshToken())
                    .expiresIn(200L)
                    .build());
        } catch (Exception e) {
            log.info("Redis not connected");
        }
    }

}
