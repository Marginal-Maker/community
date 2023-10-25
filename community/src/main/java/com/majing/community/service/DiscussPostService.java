package com.majing.community.service;

import com.majing.community.entity.DiscussPost;

import java.util.List;

/**
 * @author majing
 * @date 2023-08-02 20:44
 * @Description 评论相关的业务层逻辑接口
 */
public interface DiscussPostService {
    /**
     * 获取符合条件的评论并分页
     *
     * @param userId
     * @param offset
     * @param limit
     * @return java.util.List<com.majing.community.entity.DiscussPost>
     * @created at 2023/8/2 20:46
     */
    List<DiscussPost> getDiscussPosts(Integer userId, Integer offset, Integer limit);

    /**
     * 获取相应条件的评论数量
     *
     * @param userId
     * @return java.lang.Integer
     * @created at 2023/8/2 20:48
     */
    Integer getDiscussPostRows(Integer userId);

    /**
     * 通过分页插件获取数据
     *
     * @param userId
     * @param offset
     * @param limit
     * @return java.util.List<com.majing.community.entity.DiscussPost>
     * @created at 2023/10/21 16:05
     */
    List<DiscussPost> getDiscussPostsByPageHelper(Integer userId, Integer offset, Integer limit);

    /**
     * 发帖功能
     *
     * @param discussPost
     * @return java.lang.Integer
     * @created at 2023/10/24 15:38
     */
    Integer addDiscussPost(DiscussPost discussPost);

    /**
     * 获取帖子的详情
     *
     * @param id
     * @return java.lang.Integer
     * @created at 2023/10/25 13:54
     */
    DiscussPost getDiscussPost(Integer id);
}
