package com.majing.community.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.majing.community.dao.CommentMapper;
import com.majing.community.entity.Comment;
import com.majing.community.service.CommentService;
import com.majing.community.service.DiscussPostService;
import com.majing.community.util.CommunityConstant;
import com.majing.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

/**
 * @author majing
 * @date 2023-10-26 14:06
 * @Description
 */
@Service
public class CommentServiceImpl implements CommentService, CommunityConstant {
    private final CommentMapper commentMapper;
    private final SensitiveFilter sensitiveFilter;
    private final DiscussPostService discussPostService;
    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, SensitiveFilter sensitiveFilter, DiscussPostService discussPostService) {
        this.commentMapper = commentMapper;
        this.sensitiveFilter = sensitiveFilter;
        this.discussPostService = discussPostService;
    }

    @Override
    public PageInfo<Comment> getCommentsByEntity(Integer entityType, Integer entityId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum,pageSize)
                .doSelectPageInfo(()->commentMapper.selectCommentsByEntity(entityType, entityId));

    }
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public Integer addComment(Comment comment) {
        if(comment == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(sensitiveFilter.filter(comment.getContent())));
        Integer row = commentMapper.insertComment(comment);
        //更新帖子的评论数量
        if(comment.getEntityType().equals(ENTITY_TYPE_POST)){
            Integer count = commentMapper.selectCommentsByEntity(comment.getEntityType(), comment.getEntityId()).size();
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return row;
    }
}
