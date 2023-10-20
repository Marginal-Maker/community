package com.majing.community.controller.interceptor;

import com.majing.community.entity.LoginTicket;
import com.majing.community.entity.User;
import com.majing.community.service.UserService;
import com.majing.community.util.CookieUtil;
import com.majing.community.util.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * @author majing
 * @date 2023-09-17 19:50
 * @Description
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    private final UserService userService;
    private final HostHolder hostHolder;
    @Autowired
    public LoginTicketInterceptor(UserService userService, HostHolder hostHolder) {
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if(!StringUtils.isBlank(ticket)){
            LoginTicket loginTicket = userService.getLoginTicket(ticket);
            if(loginTicket != null && loginTicket.getStatus().equals(0) && loginTicket.getExpired().after(new Date())){
                User user = userService.getUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clean();
    }
}
