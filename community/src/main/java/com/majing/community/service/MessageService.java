package com.majing.community.service;

import com.github.pagehelper.PageInfo;
import com.majing.community.entity.Message;

import java.util.List;

/**
 * @author majing
 * @date 2023-10-31 19:19
 * @Description
 */
public interface MessageService {
    /**
     * 查询某人参与的私信列表。包括发给某人的和某人发出去的
     * @param userId
     * @param currentPage
     * @param limit
     * @return com.github.pagehelper.PageInfo<com.majing.community.entity.Message>
     * @created at 2023/11/8 13:40
    */
    PageInfo<Message> getConservations(Integer userId, Integer currentPage, Integer limit);
    /**
     * 某人参与的私信列表个数
     * @param userId
     * @return java.lang.Integer
     * @created at 2023/10/31 19:23
    */
    Integer getConservationCount(Integer userId);
    /**
     * 查询往来私信列表
     * @param conversationId
     * @param currentPage
     * @param limit
     * @return com.github.pagehelper.PageInfo<com.majing.community.entity.Message>
     * @created at 2023/11/8 13:43 
    */
    PageInfo<Message> getLetters(String conversationId, Integer currentPage, Integer limit);
    /**
     * 查询往来私信总数
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
    /**
     *
     * @param message
     * @return java.lang.Integer
     * @created at 2023/11/1 18:34
    */
    Integer addMessage(Message message);
    /**
     *
     * @param ids
     * @param status
     * @return java.lang.Integer
     * @created at 2023/11/1 18:35
    */
    Integer changeStatus(List<Integer> ids, Integer status);
}
