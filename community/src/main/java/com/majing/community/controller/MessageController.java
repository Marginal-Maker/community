package com.majing.community.controller;

import com.github.pagehelper.PageInfo;
import com.majing.community.annotation.LoginRequired;
import com.majing.community.entity.Message;
import com.majing.community.entity.Page;
import com.majing.community.entity.User;
import com.majing.community.service.MessageService;
import com.majing.community.service.UserService;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
        if (page.getCurrent() == null) {
            page.setCurrent(1);
        }
        page.setLimit(5);
        page.setPath("/conversation/list");
        page.setRows(messageService.getConservationCount(hostHolder.getUser().getId()));
        PageInfo<Message> messagePageInfo = messageService.getConservations(hostHolder.getUser().getId(), page.getCurrent(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messagePageInfo.getList())) {
            for (Message message : messagePageInfo.getList()) {
                Map<String, Object> map = new HashMap<>(4);
                map.put("conversation", message);
                map.put("letterCount", messageService.getLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.getLetterUnreadCount(hostHolder.getUser().getId(), message.getConversationId()));
                Integer targetId = hostHolder.getUser().getId().equals(message.getFromId()) ? message.getToId() : message.getFromId();
                map.put("target", userService.getUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        model.addAttribute("letterUnreadCount", messageService.getLetterUnreadCount(hostHolder.getUser().getId(), null));
        return "/site/letter";
    }

    @LoginRequired
    @RequestMapping(path = "/letter/list/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        if (page.getCurrent() == null) {
            page.setCurrent(1);
        }
        page.setLimit(5);
        page.setPath("/letter/list/" + conversationId);
        page.setRows(messageService.getLetterCount(conversationId));
        PageInfo<Message> messagePageInfo = messageService.getLetters(conversationId, page.getCurrent(), page.getLimit());
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messagePageInfo.getList())) {
            for (Message message : messagePageInfo.getList()) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("letter", message);
                map.put("fromUser", userService.getUserById(message.getFromId()));
                mapList.add(map);
            }
        }
        model.addAttribute("letters", mapList);
        model.addAttribute("target", getLetterTarget(conversationId));
        List<Integer> ids = getLetterIds(messagePageInfo.getList());
        messageService.changeStatus(ids, 1);
        return "/site/letter-detail";
    }

    private List<Integer> getLetterIds(List<Message> messageList) {
        List<Integer> ids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messageList)) {
            for (Message message : messageList){
                if(hostHolder.getUser().getId().equals(message.getToId()) && message.getStatus().equals(0)){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    public User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        Integer id0 = Integer.parseInt(ids[0]);
        Integer id1 = Integer.parseInt(ids[1]);
        if (hostHolder.getUser().getId().equals(id1)) {
            return userService.getUserById(id0);
        } else {
            return userService.getUserById(id1);
        }
    }

    @LoginRequired
    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(String toName, String content) {
        User target = userService.getUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        message.setConversationId(Integer.min(hostHolder.getUser().getId(), target.getId())
                + "_" + Integer.max(hostHolder.getUser().getId(), target.getId()));
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0);
    }
}
