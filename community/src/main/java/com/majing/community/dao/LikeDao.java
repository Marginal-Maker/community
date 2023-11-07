package com.majing.community.dao;

/**
 * @author majing
 * @date 2023-11-07 16:38
 * @Description
 */
public interface LikeDao {
    /**
     * 
     * @param userId
     * @param key
     * @return void
     * @created at 2023/11/7 16:51 
    */
    void likeAdd(String key, Integer userId);
    /**
     *
     * @param userId
     * @param key
     * @return void
     * @created at 2023/11/7 16:55
    */
    void likeRemove(String key, Integer userId);
    /**
     * 
     * @param key
     * @return java.lang.Long
     * @created at 2023/11/7 16:52 
    */
    Long likeCount(String key);
    /**
     *
     * @param userId
     * @param key
     * @return java.lang.Boolean
     * @created at 2023/11/7 16:59
    */
    Boolean getValue(String key, Integer userId);
    /**
     *
     * @param key
     * @return void
     * @created at 2023/11/7 16:53
    */
    void userLikeIncrement(String key);
    /**
     *
     * @param key
     * @return void
     * @created at 2023/11/7 16:53
    */
    void userLikeDecrement(String key);
    /**
     *
     * @param key
     * @return java.lang.Long
     * @created at 2023/11/7 16:54
    */
    Object userLikeCount(String key);
}
