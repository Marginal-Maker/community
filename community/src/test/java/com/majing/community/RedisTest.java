package com.majing.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

/**
 * @author majing
 * @date 2023-11-06 15:27
 * @Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {
    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    public RedisTest(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Test
    public void testStrings(){
        String key = "text:count";
        redisTemplate.opsForValue().set(key,1);
        System.out.println(redisTemplate.opsForValue().get(key));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().decrement(key));
    }
    @Test
    public void testTransactional(){

        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public  Object execute(RedisOperations operations) throws DataAccessException {
                String rediskey = "text:String";
                operations.multi();
                operations.opsForValue().set(rediskey,1);
                operations.opsForValue().increment(rediskey);
                return operations.exec();
            }
        });
        System.out.println(redisTemplate.opsForValue().get("text:String"));
        System.out.println(obj);
    }
}
