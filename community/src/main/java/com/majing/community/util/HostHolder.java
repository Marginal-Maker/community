package com.majing.community.util;

import com.majing.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author majing
 * @date 2023-09-17 20:07
 * @Description 持有用户信息，代替session对象
 */
@Component
public class HostHolder {
    private  final ThreadLocal<User> users = new ThreadLocal<>();
    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }

    public void clean(){
        users.remove();
    }
}
