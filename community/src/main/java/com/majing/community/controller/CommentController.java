package com.majing.community.controller;

import com.majing.community.annotation.LoginRequired;
import com.majing.community.entity.Comment;
import com.majing.community.entity.Event;
import com.majing.community.event.EventProducer;
import com.majing.community.service.CommentService;
import com.majing.community.service.DiscussPostService;
import com.majing.community.util.CommunityConstant;
import com.majing.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author majing
 * @date 2023-10-27 12:57
 * @Description
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    private final CommentService commentService;
    private final HostHolder hostHolder;
    private final EventProducer eventProducer;
    private final DiscussPostService discussPostService;
    public CommentController(CommentService commentService, HostHolder hostHolder, EventProducer eventProducer, DiscussPostService discussPostService) {
        this.commentService = commentService;
        this.hostHolder = hostHolder;
        this.eventProducer = eventProducer;
        this.discussPostService = discussPostService;
    }
    @LoginRequired
    @RequestMapping(path = "/add/{discussPostId}", method = {RequestMethod.POST, RequestMethod.GET})
    public String addComment(@PathVariable("discussPostId") Integer discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        System.out.println(eventProducer);
        Event event = new Event()
                .setTopic(EVENT_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setEntityUserId(comment.getUserId())
                .setData("discussPostId", discussPostId);
        if(comment.getEntityType().equals(ENTITY_TYPE_POST)){
            event.setEntityUserId(discussPostService.getDiscussPost(discussPostId).getUserId());
        }
        else if(comment.getEntityType().equals(ENTITY_TYPE_COMMENT)){
            if(!comment.getTargetId().equals(0)){
                event.setEntityUserId(comment.getTargetId());
            }else {
                event.setEntityUserId(commentService.findCommentById(comment.getEntityId()).getUserId());
            }
        }
        eventProducer.fireEvent(event);
        return "redirect:/discussPost/detail/" + discussPostId;
    }
}
