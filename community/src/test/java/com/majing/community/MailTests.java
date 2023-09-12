package com.majing.community;

import com.majing.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author majing
 * @date 2023-09-12 13:28
 * @Description 邮件发送测试类
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;
    @Test
    public void mailTest(){
        mailClient.sendMail("517954901@qq.com", "测试", "你好");
    }
}
