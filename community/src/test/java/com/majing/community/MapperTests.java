package com.majing.community;

import com.majing.community.dao.DiscussPostMapper;
import com.majing.community.dao.UserMapper;
import com.majing.community.entity.DiscussPost;
import com.majing.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    private final UserMapper userMapper;
    private final DiscussPostMapper discussPostMapper;
    private final User user = new User();
    @Autowired
    public MapperTests(UserMapper userMapper, DiscussPostMapper discussPostMapper) {
        this.userMapper = userMapper;
        this.discussPostMapper = discussPostMapper;
    }
    @Test
    public void testSelectUser() {
        System.out.println(userMapper.selectByEmail("nowcoder1@sina.com"));
        System.out.println(userMapper.selectById(1));
        System.out.println(userMapper.selectByName("SYSTEM"));
    }
    @Test
    public void testUpdateUser() {
        System.out.println(userMapper.updatePassword(1, "system"));
        System.out.println(userMapper.updateStatus(1, 0));
        System.out.println(userMapper.updateHeader(1, "http:/system.com"));
    }
    @Test
    public void testInsertUser(){
        user.setUsername("majing");
        user.setPassword("MAJING");
        user.setEmail("majing@qq.com");
        System.out.println(userMapper.insertUser(user));
        System.out.println(user.getId());
    }
    @Test
    public void testSelectDiscussPost(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost discussPost: discussPostList){
            System.out.println(discussPost);
        }
        System.out.println(discussPostMapper.selectDiscussPostRows(149));
    }

}
