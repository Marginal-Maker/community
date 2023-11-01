package com.majing.community.service;

import com.majing.community.entity.LoginTicket;
import com.majing.community.entity.User;

import java.util.Map;

/**
 * @author majing
 * @date 2023-08-02 20:52
 * @Description 用户相关业务逻辑接口
 */
public interface UserService {
    /**
     * 根据id获取用户
     * @param id
     * @return User
     * @created at 2023/8/2 20:53
    */
    User getUserById(Integer id);
    User getUserByName(String username);
    /**
     * 用户注册功能
     * @param user
     * @return java.lang.String
     * @created at 2023/9/12 15:23
    */
    Map<String, Object> register(User user);
    /**
     * 实现激活的业务功能
     * @param userId
     * @param code
     * @return java.lang.Integer
     * @created at 2023/9/12 17:21
    */
    Integer activation(Integer userId, String code);
    /**
     * 登录功能业务逻辑
     * @param username
     * @param password
     * @param expire
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @created at 2023/9/13 16:34
    */
    Map<String, Object> login(String username, String password, Integer expire);
    /**
     * 退出登录使得凭证过期
     * @param ticket
     * @return void
     * @created at 2023/9/15 19:42
    */
    void logout (String ticket);
    /**
     * 查询ticket对应的loginTicket实例
     * @param ticket
     * @return com.majing.community.entity.LoginTicket
     * @created at 2023/9/17 20:00
    */
    LoginTicket getLoginTicket(String ticket);
    /**
     * 更新用户头像
     * @param userId
     * @param headerUrl
     * @return java.lang.Integer
     * @created at 2023/9/17 21:18
    */
    Integer settingHeader(Integer userId, String headerUrl);
    /**
     * 更新密码
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return java.lang.Integer
     * @created at 2023/9/18 10:12
    */
    Map<String, Object> settingPassword(User user, String oldPassword, String newPassword);

}
