package com.majing.community.service;

import com.majing.community.entity.User;

/**
 * @author majing
 * @date 2023-08-02 20:52
 * @Description 用户相关业务逻辑接口
 */
public interface UserService {
    /**
     * 根据id获取用户
     * @param id
     * @return com.majing.community.entity.User
     * @created at 2023/8/2 20:53
    */
    User getUserById(Integer id);
}
