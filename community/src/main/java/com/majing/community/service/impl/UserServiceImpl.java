package com.majing.community.service.impl;

import com.majing.community.dao.LoginTicketMapper;
import com.majing.community.dao.UserMapper;
import com.majing.community.entity.LoginTicket;
import com.majing.community.entity.User;
import com.majing.community.service.UserService;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.MailClient;
import com.majing.community.util.RegularExpressionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.majing.community.util.CommunityConstant;

import java.util.*;

/**
 * @author majing
 * @date 2023-08-02 20:53
 * @Description
 */
@Service
public class UserServiceImpl implements UserService, CommunityConstant {
    private final UserMapper userMapper;
    private final MailClient mailClient;
    private final TemplateEngine templateEngine;
    private final LoginTicketMapper loginTicketMapper;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    public UserServiceImpl(UserMapper userMapper, MailClient mailClient, TemplateEngine templateEngine, LoginTicketMapper loginTicketMapper) {
        this.userMapper = userMapper;
        this.mailClient = mailClient;
        this.templateEngine = templateEngine;
        this.loginTicketMapper = loginTicketMapper;
    }
    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByName(String username) {
        return userMapper.selectByName(username);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>(1);
        //空值处理，该情况必须抛出异常
        if(user == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMessage", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMessage", "密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMessage", "邮箱不能为空");
            return map;
        }
        //验证账号
        User user1 = userMapper.selectByName(user.getUsername());
        if(user1 != null){
            map.put("usernameMessage", "用户名已经存在");
            return map;
        }
        //验证密码
        String passwordMessage = RegularExpressionUtil.verifyPassword(user.getPassword());
        if(!StringUtils.isBlank(passwordMessage)){
            map.put("passwordMessage", passwordMessage);
            return map;
        }
        user1 = userMapper.selectByEmail(user.getEmail());
        if(user1 != null){
            map.put("emailMessage", "邮箱已被注册");
            return map;
        }
        //注册用户
        //随机五个字符代表盐值
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        //加密
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        //激活码为随机字符
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //邮箱激活
        //声明Context对象，用来存数据给thymeleaf渲染
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation" + "/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        // thymeleaf渲染发送邮件的html
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }
    public Integer activation(Integer userId, String code){

        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_SUCCESS;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_REPEAT;
        }else {
            return ACTIVATION_FAILURE;
        }

    }
    @Override
    public Map<String, Object> login(String username, String password, Integer expire) {
        Map<String, Object> map = new HashMap<>(1);
        //空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMessage", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMessage", "密码不能为空");
            return map;
        }
        //验证账号
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMessage", "用户不存在");
            return map;
        }
        if (user.getStatus().equals(0)) {
            map.put("usernameMessage", "该账号未激活");
            return map;
        }
        //验证密码
        String passwordSalt = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(passwordSalt)){
            map.put("passwordMessage", "密码错误");
            return map;
        }
        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        //凭证过期时间，退出登录时凭证强制过期
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expire.longValue() * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        //返回凭证给controller层保存到cookie中，之后的请求都根据cookie传回的凭证判断是哪一个用户。本质上还是cookie-session，不是tooken
        map.put("ticket", loginTicket.getTicket());
        return map;
    }
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket, 1);
    }
    public LoginTicket getLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    @Override
    public Integer settingHeader(Integer userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }

    @Override
    public Map<String, Object> settingPassword(User user, String oldPassword ,String newPassword) {
        Map<String, Object> map = new HashMap<>(1);
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if(!StringUtils.equals(oldPassword, user.getPassword())){
            map.put("old_passwordMessage", "原密码错误");
            return map;
        }
        String passwordMessage = RegularExpressionUtil.verifyPassword(newPassword);
        if(!StringUtils.isBlank(passwordMessage)){
            map.put("new_passwordMessage", passwordMessage);
            return map;
        }
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userMapper.updatePassword(user.getId(), newPassword);
        return map;
    }


}
