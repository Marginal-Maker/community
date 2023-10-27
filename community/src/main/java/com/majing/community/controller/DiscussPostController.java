package com.majing.community.controller;

import com.github.pagehelper.PageInfo;
import com.majing.community.annotation.LoginRequired;
import com.majing.community.dao.CommentMapper;
import com.majing.community.entity.Comment;
import com.majing.community.entity.DiscussPost;
import com.majing.community.entity.Page;
import com.majing.community.entity.User;
import com.majing.community.service.CommentService;
import com.majing.community.service.DiscussPostService;
import com.majing.community.service.UserService;
import com.majing.community.util.CommunityConstant;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author majing
 * @date 2023-10-24 15:45
 * @Description
 */
@Controller
@RequestMapping("/discussPost")
public class DiscussPostController implements CommunityConstant {
    private final DiscussPostService discussPostService;
    private final HostHolder hostHolder;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public DiscussPostController(DiscussPostService discussPostService, HostHolder hostHolder, UserService userService, CommentMapper commentMapper, CommentService commentService) {
        this.discussPostService = discussPostService;
        this.hostHolder = hostHolder;
        this.userService = userService;
        this.commentService = commentService;
    }

    @LoginRequired
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "需要重新登录");
        }
        if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
            return CommunityUtil.getJSONString(403, "内容不能为空");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setType(0);
        discussPost.setStatus(0);
        discussPost.setCommentCount(0);
        discussPost.setCreateTime(new Date());
        discussPost.setScore(0.0);
        discussPostService.addDiscussPost(discussPost);
        return CommunityUtil.getJSONString(0, "发布成功");
    }

    @LoginRequired
    @RequestMapping(value = "detail/{discussPost_id}", method = RequestMethod.GET)
    public String getDiscussPostDetail(@PathVariable("discussPost_id") Integer discussPost_id, Model model, Page page) {
        DiscussPost discussPost = discussPostService.getDiscussPost(discussPost_id);
        model.addAttribute("discussPost", discussPost);
        User user = userService.getUserById(discussPost.getUserId());
        model.addAttribute("user", user);
        if(page.getCurrent() == null){
            page.setCurrent(1);
        }
        //默认每页显示10页
        if(page.getLimit()==null){
            page.setLimit(5);
        }
        page.setPath("/discussPost/detail/" + discussPost_id);
        //查询帖子id为entity_id的评论
        PageInfo<Comment> commentPageInfo = commentService.getCommentsByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getCurrent(), page.getLimit());
        page.setRows(discussPost.getCommentCount());
        List<Comment> commentList = commentPageInfo.getList();
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(commentList)){
            for(Comment comment : commentList){
                Map<String, Object> map = new HashMap<>(4);
                map.put("comment", comment);
                map.put("user", userService.getUserById(comment.getUserId()));
                //查询评论id为entity_id的帖子
                PageInfo<Comment> commentPageInfo1 = commentService.getCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String,Object>> mapList1 = new ArrayList<>();
                if(!CollectionUtils.isEmpty(commentPageInfo1.getList())){
                    for(Comment comment1 : commentPageInfo1.getList()){
                        Map<String, Object> map1 = new HashMap<>(3);
                        map1.put("user", userService.getUserById(comment1.getUserId()));
                        map1.put("replay", comment1);
                        User target =comment1.getTargetId() == 0 ? null : userService.getUserById(comment1.getTargetId());
                        map1.put("target", target);
                        mapList1.add(map1);
                        System.out.println(comment);
                    }
                }
                map.put("replays", mapList1);
                map.put("commentCount", commentPageInfo1.getTotal());
                mapList.add(map);
            }
        }
        model.addAttribute("mapList",mapList);
        return "site/discuss-detail";
    }


}
