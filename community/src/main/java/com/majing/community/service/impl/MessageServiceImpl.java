package com.majing.community.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.majing.community.dao.MessageMapper;
import com.majing.community.entity.Message;
import com.majing.community.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author majing
 * @date 2023-10-31 19:24
 * @Description
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public PageInfo<Message> getConservations(Integer userId, Integer currentPage, Integer limit) {
        PageHelper.startPage(currentPage, limit);
        List<Message> messageList = messageMapper.selectConservations(userId);
        return new PageInfo<>(messageList);
    }

    @Override
    public Integer getConservationCount(Integer userId) {
        return messageMapper.selectConservationCount(userId);
    }

    @Override
    public PageInfo<Message> getLetters(String conversationId, Integer currentPage, Integer limit) {
        PageHelper.startPage(currentPage, limit);
        List<Message> messageList = messageMapper.selectLetters(conversationId);
        return new PageInfo<>(messageList);
    }

    @Override
    public Integer getLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public Integer getLetterUnreadCount(Integer userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }
}
