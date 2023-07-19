package com.majing.community.dao;

import com.majing.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author majing
 * @date 2023-08-02 19:34
 * @Description 定义评论数据层操作
 */
@Mapper
public interface DiscussPostMapper {
    /**
     * 根据条件查询多个评论并分页显示
     * @param userId
     * @param offset
     * @param limit
     * @return java.util.List<com.majing.community.entity.DiscussPost>
     * @created at 2023/8/2 19:37
    */
    List<DiscussPost> selectDiscussPosts(Integer userId, Integer offset, Integer limit);
    /**
     * 根据条件查询当前评论总数
     * @param userId
     * @return java.lang.Integer
     * @created at 2023/8/2 19:40
    */
    Integer selectDiscussPostRows(Integer userId);

}
