package com.majing.community.controller;

import com.google.code.kaptcha.Producer;
import com.majing.community.entity.User;
import com.majing.community.service.UserService;
import com.majing.community.util.CommunityConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author majing
 * @date 2023-09-12 14:06
 * @Description 登录注册页面
 */
@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;
    private final Producer kaptchaProducer;  //根据配置类中的方法名注入

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    public LoginController(UserService userService, Producer kaptchaProducer) {
        this.userService = userService;
        this.kaptchaProducer = kaptchaProducer;
    }
    /**
     * 返回注册的页面地址
     * @param
     * @return java.lang.String
     * @created at 2023/9/13 12:59
    */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPager(){
        return "site/register";
    }
    /**
     * 注册功能中注册页面需要向页面返回的内容
     * @param model
     * @param user
     * @return java.lang.String
     * @created at 2023/9/13 13:00
    */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(CollectionUtils.isEmpty(map)){
            model.addAttribute("msg", "注册成功，已经向邮箱发送激活邮件，请尽快激活");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        }else{
            model.addAttribute("usernameMessage", map.get("usernameMessage"));
            model.addAttribute("passwordMessage", map.get("passwordMessage"));
            model.addAttribute("emailMessage", map.get("emailMessage"));
            return "site/register";
        }
    }
    /**
     * 有关激活需要向页面传递的内容
     * @param model
     * @param userId
     * @param code
     * @return java.lang.String
     * @created at 2023/9/13 13:02
    */
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
        return "site/operate-result";
    }
    /**
     * 返回登录的页面地址
     * @param
     * @return java.lang.String
     * @created at 2023/9/13 13:03
    */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPager(){
        return "/site/login";
    }
    /**
     * 验证码功能
     * @param httpServletResponse
     * @return void
     * @created at 2023/9/13 14:08
    */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse httpServletResponse, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage bufferedImage = kaptchaProducer.createImage(text);
        //将验证码存入session，cookie-session，不安全
        session.setAttribute("kaptcha", text);
        //指定传入格式
        httpServletResponse.setContentType("image/png");
        try {
            //实例化一个字节输出流对象
            OutputStream outputStream = httpServletResponse.getOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败：" + e.getMessage());
        }
    }
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, String username, String password, String code, Boolean rememberMe, HttpSession httpSession, HttpServletResponse httpServletResponse){
        if(rememberMe == null){
            rememberMe = false;
        }
        //判断验证码是否正确
        String kaptcha = (String) httpSession.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMessage", "验证码不正确");
            return "site/login";
        }
        //检查账号密码
        Integer expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            //指定凭证生效的路劲
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            httpServletResponse.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMessage", map.get("usernameMessage"));
            model.addAttribute("passwordMessage", map.get("passwordMessage"));
            return "site/login";
        }
    }
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";

    }
}
