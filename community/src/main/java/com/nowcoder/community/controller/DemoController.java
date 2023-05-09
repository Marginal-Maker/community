package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @descriotion
 * @auther majing
 * @data 2023/05/09/15:07
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello, SpringBoot";
    }
}
