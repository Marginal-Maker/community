package com.majing.community;

import com.github.pagehelper.PageInfo;
import com.majing.community.dao.CommentMapper;
import com.majing.community.dao.DiscussPostMapper;
import com.majing.community.dao.LoginTicketMapper;
import com.majing.community.dao.UserMapper;
import com.majing.community.entity.Comment;
import com.majing.community.entity.DiscussPost;
import com.majing.community.entity.LoginTicket;
import com.majing.community.entity.User;
import com.majing.community.service.CommentService;
import com.majing.community.service.DiscussPostService;
import com.majing.community.util.CommunityUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    private final UserMapper userMapper;
    private final DiscussPostMapper discussPostMapper;
    private final LoginTicketMapper loginTicketMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final User user = new User();
    @Autowired
    public MapperTests(UserMapper userMapper, DiscussPostMapper discussPostMapper, LoginTicketMapper loginTicketMapper, DiscussPostService discussPostService, CommentService commentService, CommentMapper commentMapper) {
        this.userMapper = userMapper;
        this.discussPostMapper = discussPostMapper;
        this.loginTicketMapper = loginTicketMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
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
    @Test
    public void testLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(1);
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()));
        System.out.println(loginTicketMapper.insertLoginTicket(loginTicket));
        System.out.println(loginTicketMapper.updateStatus("10a07359c9a04ee79f2792bbf0f576fa", 1));
        System.out.println(loginTicketMapper.selectByTicket("10a07359c9a04ee79f2792bbf0f576fa"));
    }
    @Test
    public void testMap(){
        Map<String, Object> map = new HashMap<>(10);
        System.out.println(CollectionUtils.isEmpty(map));
        System.out.println(map.size());
    }
    @Test
    public void testPageHelper(){
        PageInfo<Comment> pageInfo = commentService.getCommentsByEntity(1, 228, 1, 2);
        //起始页
        System.out.println(pageInfo.getPageNum());
        //每页的数量
        System.out.println(pageInfo.getPageSize());
        //总页数
        System.out.println(pageInfo.getPages());
        //总条数
        System.out.println(pageInfo.getTotal());
        //获取到的数据
        System.out.println(pageInfo.getList());
        List<Comment> commentList = commentMapper.selectCommentsByEntity(1, 228);
        for(Comment comment : commentList){
            System.out.println(comment);
        }
    }
    @Test
    public void testInsertDiscuss(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(1);
        discussPost.setTitle("1");
        discussPost.setContent("1");
        discussPost.setType(0);
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(0);
        discussPost.setScore(0.0);
        System.out.println(discussPostMapper.insertDiscussPost(discussPost));
    }

}
