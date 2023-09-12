package com.majing.community.service.impl;

import com.majing.community.dao.UserMapper;
import com.majing.community.entity.User;
import com.majing.community.service.UserService;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author majing
 * @date 2023-08-02 20:53
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final MailClient mailClient;
    private final TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    public UserServiceImpl(UserMapper userMapper, MailClient mailClient, TemplateEngine templateEngine) {
        this.userMapper = userMapper;
        this.mailClient = mailClient;
        this.templateEngine = templateEngine;
    }
    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        //空值处理
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
        if(user.getPassword().length() < 8){
            map.put("passwordMessage", "密码不能小于8位");
            return map;
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user.getPassword());
        if(!matcher.matches()){
            map.put("passwordMessage", "密码必须同时包含数字，大小写字母和特殊字符");
            return map;
        }
        user1 = userMapper.selectByEmail(user.getEmail());
        if(user1 != null){
            map.put("emailMessage", "邮箱已被注册");
            return map;
        }
        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userMapper.insertUser(user);
        //邮箱激活
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation" + "/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }
    public Integer activation(Integer userId, String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return 0;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId, 1);
            return 1;
        }else {
            return 2;
        }

    }
}
