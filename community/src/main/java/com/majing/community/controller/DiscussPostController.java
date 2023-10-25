package com.majing.community.controller;

import com.majing.community.annotation.LoginRequired;
import com.majing.community.entity.DiscussPost;
import com.majing.community.entity.User;
import com.majing.community.service.DiscussPostService;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author majing
 * @date 2023-10-24 15:45
 * @Description
 */
@Controller
@RequestMapping("/discussPost")
public class DiscussPostController {
    private final DiscussPostService discussPostService;
    private final HostHolder hostHolder;

    @Autowired
    public DiscussPostController(DiscussPostService discussPostService, HostHolder hostHolder) {
        this.discussPostService = discussPostService;
        this.hostHolder = hostHolder;
    }

    @LoginRequired
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "需要重新登录");
        }
        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
            return CommunityUtil.getJSONString(403, "内容不能为空");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setType(0);
        discussPost.setStatus(0);
        discussPost.setCreateTime(new Date());
        discussPost.setScore(0.0);
        discussPostService.addDiscussPost(discussPost);
        return CommunityUtil.getJSONString(0, "发布成功");
    }

}
