package com.majing.community.service.impl;

import com.majing.community.entity.User;
import com.majing.community.service.FollowService;
import com.majing.community.service.UserService;
import com.majing.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author majing
 * @date 2023-11-08 11:59
 * @Description
 */
@Service
public class FollowServiceImpl implements FollowService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    @Autowired
    public FollowServiceImpl(RedisTemplate<String, Object> redisTemplate, UserService userService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }

    @Override
    public void follow(Integer userId, Integer entityType, Integer entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followedKey = RedisKeyUtil.getFollowed(userId, entityType);
                String followerKey = RedisKeyUtil.getFollower(entityId, entityType);
                operations.multi();
                operations.opsForZSet().add(followedKey, entityId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    @Override
    public void unfollow(Integer userId, Integer entityType, Integer entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followedKey = RedisKeyUtil.getFollowed(userId, entityType);
                String followerKey = RedisKeyUtil.getFollower(entityId, entityType);
                operations.multi();
                operations.opsForZSet().remove(followedKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);
                return operations.exec();
            }
        });
    }

    @Override
    public Long findFollowed(Integer userId, Integer entityType) {
        String followedKey = RedisKeyUtil.getFollowed(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followedKey);
    }

    @Override
    public Long findFollower(Integer entityId, Integer entityType) {
        String followerKey = RedisKeyUtil.getFollower(entityId, entityType);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    @Override
    public Boolean hasFollow(Integer userId, Integer entityId, Integer entityType) {
        String followerKey = RedisKeyUtil.getFollower(entityId, entityType);
        return redisTemplate.opsForZSet().score(followerKey, userId) != null;
    }

    @Override
    public List<Map<String, Object>> findFollowedORFollower(String key, int offset, int limit) {
        Set<Object> targetIds = redisTemplate.opsForZSet().reverseRange(key, offset, offset + limit - 1);
        if(CollectionUtils.isEmpty(targetIds)){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for(Object targetId : targetIds){
            Map<String, Object> map = new HashMap<>(2);
            User user = userService.getUserById((Integer) targetId);
            map.put("user", user);
            map.put("followTime", new Date(Objects.requireNonNull(redisTemplate.opsForZSet().score(key, targetId)).longValue()));
            list.add(map);
        }
        return list;
    }
}
