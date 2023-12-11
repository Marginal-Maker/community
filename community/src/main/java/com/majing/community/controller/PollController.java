package com.majing.community.controller;

import com.majing.community.servlet.AsyncLongPollingServlet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author majing
 * @date 2023-11-27 20:25
 * @Description
 */
@Controller
public class PollController {
    @RequestMapping({"/polling"})
    public String index(){
        System.out.println("IndexController.index");
        return "polling";
    }


    @RequestMapping({"/publishMsg"})
    @ResponseBody
    public String publishMsg(String message){
        AsyncLongPollingServlet.message  = message;
        return "OK";
    }
}
