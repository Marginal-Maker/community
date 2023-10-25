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
     * 根据条件查询多个评论并分页显示（手动实现）
     *
     * @param userId
     * @param offset
     * @param limit
     * @return java.util.List<com.majing.community.entity.DiscussPost>
     * @created at 2023/8/2 19:37
     */
    List<DiscussPost> selectDiscussPosts(Integer userId, Integer offset, Integer limit);

    /**
     * 根据条件查询当前评论总数
     *
     * @param userId
     * @return java.lang.Integer
     * @created at 2023/8/2 19:40
     */
    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且在<if>中使用，则必须要加别名
    Integer selectDiscussPostRows(Integer userId);

    /**
     * 通过分页插件实现分页查询功能
     *
     * @param userId
     * @return java.util.List<com.majing.community.entity.DiscussPost>
     * @created at 2023/10/21 15:44
     */
    List<DiscussPost> selectDiscussPostByPageHelper(Integer userId);

    /**
     * 插入新帖子
     *
     * @param discussPost
     * @return java.lang.Integer
     * @created at 2023/10/24 15:07
     */
    Integer insertDiscussPost(DiscussPost discussPost);
    /**
     * 根据主键查询对应的帖子
     * @param id
     * @return java.lang.Integer
     * @created at 2023/10/25 13:51
    */
    DiscussPost selectDiscussPostById(Integer id);

}
