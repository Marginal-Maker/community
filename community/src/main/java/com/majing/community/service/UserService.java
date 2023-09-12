package com.majing.community.service;

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
}
