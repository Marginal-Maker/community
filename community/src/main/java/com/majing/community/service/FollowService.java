package com.majing.community.service;

import java.util.List;
import java.util.Map;

/**
 * @author majing
 * @date 2023-11-08 11:58
 * @Description
 */
public interface FollowService {
    void follow(Integer userId, Integer entityType, Integer entityId);
    void unfollow(Integer userId, Integer entityType, Integer entityId);
    Long findFollowed(Integer userId, Integer entityType);
    Long findFollower(Integer entityId, Integer entityType);
    Boolean hasFollow(Integer userId, Integer entityId, Integer entityType);
    List<Map<String, Object>> findFollowedORFollower(String key, int offset, int limit);
}
