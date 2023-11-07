package com.majing.community.dao.impl;

import com.majing.community.dao.LikeDao;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author majing
 * @date 2023-11-07 16:38
 * @Description
 */
@Repository
public class LikeDaoImpl implements LikeDao {
    private final RedisTemplate<String, Object> redisTemplate;

    public LikeDaoImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void likeAdd(String key, Integer userId) {
        redisTemplate.opsForSet().add(key, userId);
    }

    @Override
    public void likeRemove(String key, Integer userId) {
        redisTemplate.opsForSet().remove(key, userId);
    }

    @Override
    public Long likeCount(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Boolean getValue(String key, Integer userId) {
        return redisTemplate.opsForSet().isMember(key, userId);
    }

    @Override
    public void userLikeIncrement(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public void userLikeDecrement(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    @Override
    public Object userLikeCount(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
