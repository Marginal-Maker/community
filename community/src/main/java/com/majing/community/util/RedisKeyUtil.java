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
    //某个实体的赞
    //Like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(Integer entityType, Integer entityId){
       return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT +entityId;
    }
    public static String getUserLikeKey(Integer userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }
}
