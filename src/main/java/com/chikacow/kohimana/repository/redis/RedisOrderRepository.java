package com.chikacow.kohimana.repository.redis;

import com.chikacow.kohimana.model.redis.RedisOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisOrderRepository extends CrudRepository<RedisOrder, String> {

}
