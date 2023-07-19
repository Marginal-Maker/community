package com.majing.community.dao;

import com.majing.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author majing
 * @date 2023-08-02 15:53
 * @Description 定义用户数据层的操作
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户Id查询
     * @param id
     * @return com.majing.community.entity.User
     * @created at 2023/8/2 19:03
     */
    User selectById(Integer id);
    /**
     * 根据用户名称查询
     * @param username
     * @return com.majing.community.entity.User
     * @created at 2023/8/2 19:06
     */
    User selectByName(String username);
    /**
     * 根据用户邮箱查询
     * @param email
     * @return com.majing.community.entity.User
     * @created at 2023/8/2 19:13
    */
    User selectByEmail(String email);
    /**
     * 插入用户
     * @param user
     * @return java.lang.Integer
     * @created at 2023/8/2 19:13
    */
    Integer insertUser(User user);
    /**
     * 更新用户状态
     * @param id
     * @param status
     * @return java.lang.Integer
     * @created at 2023/8/2 19:08
     */
    Integer updateStatus(Integer id, Integer status);
    /**
     * 更新用户头像
     * @param id
     * @param headerUrl
     * @return java.lang.Integer
     * @created at 2023/8/2 19:13`
    */
    Integer updateHeader(Integer id, String headerUrl);
    /**
     * 更新密码
     * @param id
     * @param password
     * @return java.lang.Integer
     * @created at 2023/8/2 19:22
    */
    Integer updatePassword(Integer id, String password);

}
