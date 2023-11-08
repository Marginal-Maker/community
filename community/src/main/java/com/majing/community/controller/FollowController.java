package com.majing.community.controller;

import com.majing.community.entity.Page;
import com.majing.community.entity.User;
import com.majing.community.service.FollowService;
import com.majing.community.service.UserService;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.HostHolder;
import com.majing.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author majing
 * @date 2023-11-08 12:16
 * @Description
 */
@Controller
public class FollowController {
    private final FollowService followService;
    private final HostHolder hostHolder;
    private final UserService userService;
    @Autowired
    public FollowController(FollowService followService, HostHolder hostHolder, UserService userService) {
        this.followService = followService;
        this.hostHolder = hostHolder;
        this.userService = userService;
    }
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(Integer entityType, Integer entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(0, "已关注");
    }
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(Integer entityType, Integer entityId){
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(0, "已取消关注");
    }
    @RequestMapping(path = "/followList/{followType}/{userId}", method = RequestMethod.GET)
    public String followed(@PathVariable("userId") Integer userId, @PathVariable("followType") String followType, Page page, Model model) throws Exception {
        User currentUser = userService.getUserById(userId);
        if(currentUser == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("currentUser", currentUser);
        if(page.getCurrent()==null){
            page.setCurrent(1);
        }
        page.setLimit(5);
        page.setPath("/followed/"+ followType + "/" + userId);
        page.setRows(followService.findFollowed(userId,3).intValue());
        Class<?> redisKeyUtilClass = Class.forName("com.majing.community.util.RedisKeyUtil");
        Method method = redisKeyUtilClass.getMethod("get"+followType, Integer.class, Integer.class);
        String key = (String) method.invoke(null,userId,3);
        List<Map<String, Object>> userList = followService.findFollowedORFollower(key, page.getOffSet(), page.getLimit());
        if(!CollectionUtils.isEmpty(userList)){
            for(Map<String, Object> map : userList){
                User user = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(user.getId()));
            }
        }
        model.addAttribute("users", userList);
        return followType.equals("Followed") ? "/site/followee" :"/site/follower";
    }

    private boolean hasFollowed(Integer userId){
        if(hostHolder.getUser().getId()==null){
            return false;
        }
        return followService.hasFollow(hostHolder.getUser().getId(), userId, 3);
    }
}
