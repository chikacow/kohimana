package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Token;
import com.chikacow.kohimana.model.redis.RedisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
}
