package com.nowcoder.community.dao.impl;

import com.nowcoder.community.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * @descriotion
 * @auther majing
 * @data 2023/05/09/19:02
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public String result() {
        return "hello";
    }
}
