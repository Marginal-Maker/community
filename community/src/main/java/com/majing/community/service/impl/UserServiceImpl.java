package com.majing.community.service.impl;

import com.majing.community.dao.UserMapper;
import com.majing.community.entity.User;
import com.majing.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author majing
 * @date 2023-08-02 20:53
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }
}
