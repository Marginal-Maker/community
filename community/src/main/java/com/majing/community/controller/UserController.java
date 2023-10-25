package com.majing.community.controller;

import com.majing.community.annotation.LoginRequired;
import com.majing.community.service.UserService;
import com.majing.community.util.CommunityUtil;
import com.majing.community.util.HostHolder;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

/**
 * @author majing
 * @date 2023-09-17 21:04
 * @Description
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final HostHolder hostHolder;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    public UserController(UserService userService, HostHolder hostHolder) {
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    @LoginRequired
    public String getSettingPage(){
        return "site/setting";
    }
    @RequestMapping(path = "/uploadHeader", method = {RequestMethod.POST, RequestMethod.GET})
    @LoginRequired
    public String uploadHeader(MultipartFile multipartFile, Model model){
        if(multipartFile == null){
            model.addAttribute("error", "请选择图片！");
            return "site/setting";
        }
        //获取文件名，如1.png
        String fileName = multipartFile.getOriginalFilename();
        String suffix = null;
        if (fileName != null) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        if(StringUtils.isBlank(suffix) || !StringUtils.contains("png,jpg,jpeg", suffix)){
            model.addAttribute("error", "图片格式不对！");
            return "site/setting";
        }
        fileName = CommunityUtil.generateUUID() + '.' + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }

        String headUrl = domain + contextPath + "/user/header/" + fileName;
        userService.settingHeader(hostHolder.getUser().getId(), headUrl);
        return "redirect:/index";
    }
    @RequestMapping(value = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse httpServletResponse){
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        httpServletResponse.setContentType("image/" + suffix);
        try (FileInputStream fileInputStream = new FileInputStream(fileName)){
            OutputStream outputStream = httpServletResponse.getOutputStream();
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
        }
    }
    @RequestMapping(value = "/updatePassword", method = {RequestMethod.POST, RequestMethod.GET})
    @LoginRequired
    public String updatePassword(Model model, String old_password, String new_password){
        if(StringUtils.isBlank(old_password) || StringUtils.isBlank(new_password)){
            throw new IllegalArgumentException("参数不能为空");
        }
        Map<String, Object> map = userService.settingPassword(hostHolder.getUser(), old_password, new_password);
        if(CollectionUtils.isEmpty(map)){
            return "redirect:/index";
        }
        else{
            model.addAttribute("old_passwordMessage", map.get("old_passwordMessage"));
            model.addAttribute("new_passwordMessage", map.get("new_passwordMessage"));
            return "site/setting";
        }
    }

}
