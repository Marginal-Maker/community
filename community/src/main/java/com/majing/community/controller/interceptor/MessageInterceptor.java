package com.majing.community.controller.interceptor;

import com.majing.community.entity.User;
import com.majing.community.service.MessageService;
import com.majing.community.util.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author majing
 * @date 2023-11-16 18:29
 * @Description
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {
    private final HostHolder hostHolder;
    private final MessageService messageService;

    public MessageInterceptor(HostHolder hostHolder, MessageService messageService) {
        this.hostHolder = hostHolder;
        this.messageService = messageService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            Integer count = messageService.findUnreadNoticeCount(user.getId(), null)+
                    messageService.getLetterUnreadCount(user.getId(), null);
            modelAndView.addObject("allUnreadCount", count);
        }
    }
}
