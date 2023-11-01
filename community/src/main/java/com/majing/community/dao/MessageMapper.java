package com.majing.community.dao;

import com.majing.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author majing
 * @date 2023-10-31 16:12
 * @Description
 */
@Mapper
public interface MessageMapper {
    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的消息
     *
     * @param userId
     * @return java.util.List<com.majing.community.entity.Message>
     * @created at 2023/10/31 16:18
     */
    List<Message> selectConservations(Integer userId);

    /**
     * 查询当前用户的会话数量
     *
     * @param userId
     * @return java.lang.Integer
     * @created at 2023/10/31 16:19
     */
    Integer selectConservationCount(Integer userId);

    /**
     * 查询某个会话所包含的私信列表
     *
     * @param conservationId
     * @return java.util.List<com.majing.community.entity.Message>
     * @created at 2023/10/31 16:19
     */
    List<Message> selectLetters(String conservationId);

    /**
     * 获取某个会话的私信数量
     *
     * @param conservationId
     * @return java.lang.Integer
     * @created at 2023/10/31 16:20
     */
    Integer selectLetterCount(String conservationId);

    /**
     * 查询私信未读数量，分所有的未读和某个会话的未读数量
     *
     * @param userId
     * @param conversationId
     * @return java.lang.Integer
     * @created at 2023/10/31 16:21
     */
    Integer selectLetterUnreadCount(Integer userId, String conversationId);
    /**
     *
     * @param message
     * @return java.lang.Integer
     * @created at 2023/11/1 18:24
    */
    Integer insertMessage(Message message);
    /**
     *
     * @param ids
     * @param status
     * @return java.lang.Integer
     * @created at 2023/11/1 18:27
    */
    Integer updateStatus(List<Integer> ids, Integer status);
}
