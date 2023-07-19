package com.majing.community.service.impl;

import com.majing.community.dao.DiscussPostMapper;
import com.majing.community.entity.DiscussPost;
import com.majing.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author majing
 * @date 2023-08-02 20:49
 * @Description 接口实现类
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    private final DiscussPostMapper discussPostMapper;
    @Autowired
    public DiscussPostServiceImpl(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }

    @Override
    public List<DiscussPost> getDiscussPosts(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    @Override
    public Integer getDiscussPostRows(Integer userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
