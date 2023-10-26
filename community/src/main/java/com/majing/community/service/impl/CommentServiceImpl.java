package com.majing.community.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.majing.community.dao.CommentMapper;
import com.majing.community.entity.Comment;
import com.majing.community.entity.Page;
import com.majing.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author majing
 * @date 2023-10-26 14:06
 * @Description
 */
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public PageInfo<Comment> getCommentsByEntity(Integer entityType, Integer entityId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum,pageSize)
                .doSelectPageInfo(()->commentMapper.selectCommentsByEntity(entityType, entityId));

    }
}
