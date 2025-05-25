package com.chikacow.kohimana.service;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.redis.RedisToken;
import com.chikacow.kohimana.repository.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTokenRepository redisTokenRepository;

    public String save(RedisToken redisToken) {
        RedisToken save = redisTokenRepository.save(redisToken);
        return save.getId();
    }

    public void delete(String id) {
        redisTokenRepository.deleteById(id);
    }

    public RedisToken getById(String id) {
        return redisTokenRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Token not fount in redis"));
    }
}
