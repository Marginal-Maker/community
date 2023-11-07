package com.majing.community.service.impl;

import com.majing.community.dao.LikeDao;
import com.majing.community.service.LikeService;
import com.majing.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author majing
 * @date 2023-11-06 19:06
 * @Description
 */
@Service
public class LikeServiceImpl implements LikeService {
    private final LikeDao likeDao;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public LikeServiceImpl(LikeDao likeDao, RedisTemplate<String, Object> redisTemplate) {
        this.likeDao = likeDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();
                if (Boolean.TRUE.equals(isMember)) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                operations.exec();
                return null;
            }
        });
    }

    @Override
    public Long likeCount(Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Long count = likeDao.likeCount(entityLikeKey);
        if (count != null) {
            return count;
        }
        return 0L;
    }

    @Override
    public Integer entityLikeType(Integer userId, Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        if (Boolean.TRUE.equals(likeDao.getValue(entityLikeKey, userId))) {
            return 1;
        }
        return 0;
    }

    @Override
    public Long userLikeCount(Integer entityUserId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
        Integer result = (Integer) likeDao.userLikeCount(userLikeKey);
        return result == null ? 0 : result.longValue();
    }

}
