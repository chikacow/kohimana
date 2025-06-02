package com.chikacow.kohimana.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisConfigurer {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Redis connection factory initialized");
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);

    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), "localhost", 6379);

    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<String, Long> redisRLTemplate() {
//        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        return redisTemplate;
//    }
    @Bean
    public RedisTemplate<String, Long> redisRLTemplate() {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        // Use String serializer for keys
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Use String serializer for values (or generic to string for Long)
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));

        return redisTemplate;
    }

}
