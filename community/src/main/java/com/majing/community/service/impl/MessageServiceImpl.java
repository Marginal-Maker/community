package com.majing.community.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.majing.community.dao.MessageMapper;
import com.majing.community.entity.Message;
import com.majing.community.service.MessageService;
import com.majing.community.util.SensitiveFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author majing
 * @date 2023-10-31 19:24
 * @Description
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final SensitiveFilter sensitiveFilter;

    public MessageServiceImpl(MessageMapper messageMapper, SensitiveFilter sensitiveFilter) {
        this.messageMapper = messageMapper;
        this.sensitiveFilter = sensitiveFilter;
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

    @Override
    public Integer addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(sensitiveFilter.filter(message.getContent())));
        return messageMapper.insertMessage(message);
    }

    @Override
    public Integer changeStatus(List<Integer> ids, Integer status) {
        return messageMapper.updateStatus(ids, status);
    }
    public Message findLatestNotice(Integer userId, String topic){
        return messageMapper.selectLatestNotice(userId, topic);
    }

    @Override
    public Integer findNoticeCount(Integer userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    @Override
    public Integer findUnreadNoticeCount(Integer userId, String topic) {
        return messageMapper.selectUnreadNoticeCount(userId, topic);
    }

    @Override
    public PageInfo<Message> findNotices(Integer userId, String topic, Integer pageCurrent, Integer limit) {
        PageHelper.startPage(pageCurrent, limit);
        List<Message> messageList = messageMapper.selectNotices(userId, topic);
        return new PageInfo<>(messageList);
    }
}
