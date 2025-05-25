package com.chikacow.kohimana.model.redis;

import com.chikacow.kohimana.model.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RedisToken")
public class RedisToken implements Serializable {

    private String id;
    private String accessToken;
    private String refreshToken;
    private String resetToken;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiresIn;

}