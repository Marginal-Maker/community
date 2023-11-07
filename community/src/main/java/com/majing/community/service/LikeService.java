package com.majing.community.service;

import org.slf4j.LoggerFactory;

/**
 * @author majing
 * @date 2023-11-06 19:05
 * @Description
 */
public interface LikeService {
    /**
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return void
     * @created at 2023/11/6 19:37
    */
    void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId);
    /**
     *
     * @param entityType
     * @param entityId
     * @return long
     * @created at 2023/11/6 19:38
    */
    Long likeCount(Integer entityType, Integer entityId);
    /**
     * 
     * @param userId
     * @param entityType
     * @param entityId
     * @return java.lang.Integer
     * @created at 2023/11/6 19:43
    */
    Integer entityLikeType(Integer userId, Integer entityType, Integer entityId);
    /**
     * 
     * @param entityUserId
     * @return java.lang.Long
     * @created at 2023/11/7 17:26 
    */
    Long userLikeCount(Integer entityUserId);
}
