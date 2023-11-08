package com.majing.community.dao;

import com.majing.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author majing
 * @date 2023-10-26 13:34
 * @Description
 */
@Mapper
public interface CommentMapper {
    /**
     * 
     * @param entityType
     * @param entityId
     * @return java.util.List<com.majing.community.entity.Comment>
     * @created at 2023/10/27 12:18 
    */
    List<Comment> selectCommentsByEntity(Integer entityType, Integer entityId);
    /**
     * 
     * @param comment
     * @return java.lang.Integer
     * @created at 2023/10/27 12:18 
    */
    Integer insertComment(Comment comment);
    /**
     * 
     * @param id
     * @return com.majing.community.entity.Comment
     * @created at 2023/11/15 13:17
    */
    Comment selectCommentById(Integer id);
}
