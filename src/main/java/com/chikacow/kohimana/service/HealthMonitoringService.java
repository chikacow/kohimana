package com.chikacow.kohimana.service;

import com.chikacow.kohimana.a_rate_limiting.strategy.constant.TokenBucketConstant;
import io.minio.MinioClient;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
@Slf4j
@RequiredArgsConstructor
public class HealthMonitoringService {

    @Value("${spring.datasource.username}")
    private String mySqlUsername;

    @Value("${spring.datasource.password}")
    private String mySqlPassword;

    @Value("${spring.datasource.url}")
    private String mySqlUrl;

    private final JedisPool jedisPool;

    private final MinioClient minioClient;

    public boolean isRedisUp() {
        try (Jedis jedis = jedisPool.getResource()) {
            String response = jedis.ping();
            return "PONG".equalsIgnoreCase(response);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMySqlDatabaseUp() {
        try (Connection conn = DriverManager.getConnection(mySqlUrl, mySqlUsername, mySqlPassword)) {
            if (conn != null) {
                System.out.println("Connected to MySQL");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return false;
    }

    public boolean isMinioUp() {
        try {
            minioClient.listBuckets(); // this will throw if not connected or unauthorized
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String checkRateLimitStatus() {

        return TokenBucketConstant.getMAX_TOKEN().toString() + " requests per " + "50" + " seconds";
    }



}
