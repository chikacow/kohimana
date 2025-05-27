package com.chikacow.kohimana.model.redis;


import com.chikacow.kohimana.model.OrderItem;
import com.chikacow.kohimana.model.Payment;
import com.chikacow.kohimana.model.Seat;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RedisOrder")
public class RedisOrder {

    private String id;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiresIn;

}
