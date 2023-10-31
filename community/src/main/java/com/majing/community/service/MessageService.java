package com.majing.community.service;

import com.github.pagehelper.PageInfo;
import com.majing.community.entity.Message;

/**
 * @author majing
 * @date 2023-10-31 19:19
 * @Description
 */
public interface MessageService {
    /**
     *
     * @param userId
     * @param offset
     * @param limit
     * @return java.util.List<com.majing.community.entity.Message>
     * @created at 2023/10/31 19:23
    */
    PageInfo<Message> getConservations(Integer userId, Integer currentPage, Integer limit);
    /**
     *
     * @param userId
     * @return java.lang.Integer
     * @created at 2023/10/31 19:23
    */
    Integer getConservationCount(Integer userId);
    /**
     *
     * @param conversationId
     * @param offset
     * @param limit
     * @return java.util.List<com.majing.community.entity.Message>
     * @created at 2023/10/31 19:23
    */
    PageInfo<Message> getLetters(String conversationId, Integer currentPage, Integer limit);
    /**
     *
     * @param conversationId
     * @return java.lang.Integer
     * @created at 2023/10/31 19:24
    */
    Integer getLetterCount(String conversationId);
    /**
     *
     * @param userId
     * @param conversationId
     * @return java.lang.Integer
     * @created at 2023/10/31 19:24
    */
    Integer getLetterUnreadCount(Integer userId, String conversationId);
}
