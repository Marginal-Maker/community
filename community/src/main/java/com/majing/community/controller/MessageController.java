package com.majing.community.controller;

import com.github.pagehelper.PageInfo;
import com.majing.community.annotation.LoginRequired;
import com.majing.community.entity.Message;
import com.majing.community.entity.Page;
import com.majing.community.service.MessageService;
import com.majing.community.service.UserService;
import com.majing.community.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author majing
 * @date 2023-10-31 20:57
 * @Description
 */
@Controller
public class MessageController {
    private final MessageService messageService;
    private final HostHolder hostHolder;
    private final UserService userService;

    public MessageController(MessageService messageService, HostHolder hostHolder, UserService userService) {
        this.messageService = messageService;
        this.hostHolder = hostHolder;
        this.userService = userService;
    }

    @LoginRequired
    @RequestMapping(path = "/conversation/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page) {
        if(page.getCurrent() == null){
            page.setCurrent(1);
        }
        page.setLimit(5);
        page.setPath("/conversation/list");
        page.setRows(messageService.getConservationCount(hostHolder.getUser().getId()));
        PageInfo<Message> messagePageInfo = messageService.getConservations(hostHolder.getUser().getId(), page.getCurrent(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if(!CollectionUtils.isEmpty(messagePageInfo.getList())){
            for(Message message: messagePageInfo.getList()){
                Map<String, Object> map = new HashMap<>(4);
                map.put("conversation", message);
                map.put("letterCount", messageService.getLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.getLetterUnreadCount(hostHolder.getUser().getId(), message.getConversationId()));
                Integer targetId = hostHolder.getUser().getId().equals(message.getFromId()) ? message.getToId(): message.getFromId();
                map.put("target", userService.getUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        model.addAttribute("letterUnreadCount", messageService.getLetterUnreadCount(hostHolder.getUser().getId(), null));
        return "/site/letter";
    }
}
