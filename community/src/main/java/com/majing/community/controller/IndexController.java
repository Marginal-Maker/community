package com.majing.community.controller;

import com.majing.community.entity.DiscussPost;
import com.majing.community.entity.Page;
import com.majing.community.entity.User;
import com.majing.community.service.DiscussPostService;
import com.majing.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author majing
 * @date 2023-08-02 21:00
 * @Description index页面控制层
 */
@Controller
public class IndexController {
    private final UserService userService;
    private final DiscussPostService discussPostService;
    @Autowired
    public IndexController(UserService userService, DiscussPostService discussPostService) {
        this.userService = userService;
        this.discussPostService = discussPostService;
    }
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getDiscussPostsPage(Model model, Page page){
        if(page.getCurrent() == null){
            page.setCurrent(1);
        }
        if(page.getLimit()==null){
            page.setLimit(10);
        }
        page.setRows(discussPostService.getDiscussPostRows(null));
        page.setPath("/index");
        List<DiscussPost> discussPostList = discussPostService.getDiscussPosts(null, page.getOffSet(), page.getLimit());
        List<Map<String, Object>> userDiscussPosts = new ArrayList<>();
        if(discussPostList != null && discussPostList.size()>0){
            for(DiscussPost discussPost: discussPostList){
                Map<String, Object> map = new HashMap<>(2);
                map.put("discussPost", discussPost);
                User user = userService.getUserById(discussPost.getUserId());
                map.put("user", user);
                userDiscussPosts.add(map);
            }
        }
        model.addAttribute("userDiscussPosts", userDiscussPosts);
        return "index";
    }
}
