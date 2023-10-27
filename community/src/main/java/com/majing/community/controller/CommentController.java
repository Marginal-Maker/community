package com.majing.community.controller;

import com.majing.community.annotation.LoginRequired;
import com.majing.community.entity.Comment;
import com.majing.community.service.CommentService;
import com.majing.community.util.HostHolder;
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
public class CommentController {
    private final CommentService commentService;
    private final HostHolder hostHolder;

    public CommentController(CommentService commentService, HostHolder hostHolder) {
        this.commentService = commentService;
        this.hostHolder = hostHolder;
    }
    @LoginRequired
    @RequestMapping(value = "/add/{discussPostId}", method = {RequestMethod.POST, RequestMethod.GET})
    public String addComment(@PathVariable("discussPostId") Integer discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        return "redirect:/discussPost/detail/" + discussPostId;
    }
}
