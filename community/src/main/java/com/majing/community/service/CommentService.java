package com.majing.community.service;
import com.github.pagehelper.PageInfo;
import com.majing.community.entity.Comment;

import java.util.List;

/**
 * @author majing
 * @date 2023-10-26 13:48
 * @Description
 */

public interface CommentService {
    /**
     * 
     * @param entityType
     * @param entityId
     * @param pageNum
     * @param pageSize
     * @return com.github.pagehelper.PageInfo<com.majing.community.entity.Comment>
     * @created at 2023/10/26 15:09
    */
    PageInfo<Comment> getCommentsByEntity(Integer entityType, Integer entityId, Integer pageNum, Integer pageSize);
}
