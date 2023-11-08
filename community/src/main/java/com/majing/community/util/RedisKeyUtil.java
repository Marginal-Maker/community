package com.majing.community.util;

/**
 * @author majing
 * @date 2023-11-06 18:59
 * @Description
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWED = "followed";
    private static final String PREFIX_FOLLOWER = "follower";
    //某个实体的赞
    //Like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(Integer entityType, Integer entityId){
       return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT +entityId;
    }
    public static String getUserLikeKey(Integer userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }
    public static String getFollowed(Integer userId, Integer entityType){
        return PREFIX_FOLLOWED + SPLIT + userId + SPLIT + entityType;
    }
    public static String getFollower(Integer entityId, Integer entityType){
        return PREFIX_FOLLOWER + SPLIT + entityId + SPLIT + entityType;
    }
}
