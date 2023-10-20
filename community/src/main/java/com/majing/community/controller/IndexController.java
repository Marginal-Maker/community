package com.majing.community.controller;

import com.majing.community.entity.DiscussPost;
import com.majing.community.entity.Page;
import com.majing.community.entity.User;
import com.majing.community.service.DiscussPostService;
import com.majing.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author majing
 * @date 2023-08-02 21:00
 * @Description 开发社区首页控制层
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
    /**
     * 首页显示discussPost
     * @param model
     * @param page
     * @return java.lang.String
     * @created at 2023/9/11 17:19
    */
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getDiscussPostsPage(Model model, Page page){
        //初始化页面时，没有page传入，此时设置page为1
        if(page.getCurrent() == null){
            page.setCurrent(1);
        }
        //默认每页显示10页
        if(page.getLimit()==null){
            page.setLimit(10);
        }
        //获取总行数，如果前端判断总行数为0，则index.html中分页部分不显示
        page.setRows(discussPostService.getDiscussPostRows(null));
        //指定页面跳转时的访问路劲，当前端点击某一页时和当前页数拼接成url，即http://localhost:8080/community/index?current=1
        page.setPath("/index");
        //获取符合条件的某页数据，整合为一个List集合，元素类型为Map集合
        List<DiscussPost> discussPostList = discussPostService.getDiscussPosts(null, page.getOffSet(), page.getLimit());
        List<Map<String, Object>> userDiscussPosts = new ArrayList<>();
        if(discussPostList != null && discussPostList.size()>0){
            for(DiscussPost discussPost: discussPostList){
                //定义长度为2的Map集合，一个位置保存评论，一个位置保存评论对应的用户对象。另一种方法即在sql语句是使用联合查询，但为了后续使用redis,进行两次查询
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
