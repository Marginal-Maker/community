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
    List<Comment> selectCommentsByEntity(Integer entityType, Integer entityId);
}
