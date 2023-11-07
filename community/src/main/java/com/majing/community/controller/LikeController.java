package com.majing.community.controller;

import com.majing.community.annotation.LoginRequired;
import com.majing.community.entity.User;
import com.majing.community.service.LikeService;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author majing
 * @date 2023-11-07 12:24
 * @Description
 */
@Controller
public class LikeController {
    private final LikeService likeService;
    private final HostHolder hostHolder;
    @Autowired
    public LikeController(LikeService likeService, HostHolder hostHolder) {
        this.likeService = likeService;
        this.hostHolder = hostHolder;
    }
    @RequestMapping(path = "/like", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String like(Integer entityType, Integer entityId, Integer entityUserId){
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        Long likeCount = likeService.likeCount(entityType, entityId);
        Integer status = likeService.entityLikeType(user.getId(), entityType, entityId);
        Map<String, Object> map = new HashMap<>(2);
        map.put("likeCount", likeCount);
        map.put("likeStatus", status);
        return CommunityUtil.getJSONString(0, null, map);
    }

}
