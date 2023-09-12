package com.majing.community.controller;

import com.majing.community.entity.User;
import com.majing.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;

/**
 * @author majing
 * @date 2023-09-12 14:06
 * @Description 登录页面
 */
@Controller
public class LoginController {
    UserService userService;
    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPager(){
        return "/site/register";
    }
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(CollectionUtils.isEmpty(map)){
            model.addAttribute("msg", "注册成功，已经向邮箱发送激活邮件，请尽快激活");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMessage", map.get("usernameMessage"));
            model.addAttribute("passwordMessage", map.get("passwordMessage"));
            model.addAttribute("emailMessage", map.get("emailMessage"));
            return "/site/register";
        }
    }
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") Integer userId, @PathVariable("code") String code){
        Integer result = userService.activation(userId, code);
        if(result == 1){
            model.addAttribute("msg", "激活成功，账号已经可以正常使用");
            model.addAttribute("target", "/login");
        } else if (result == 0) {
            model.addAttribute("msg", "账号已经激活了，无序重复激活");
            model.addAttribute("target", "/index");
        }else{
            model.addAttribute("msg", "激活失败，激活码不正确");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPager(){
        return "/site/login";
    }
}
