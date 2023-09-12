package com.majing.community.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @author majing
 * @date 2023-09-12 13:10
 * @Description 发送邮件的实现类
 */
@Component
public class MailClient {
    /**创建MailClient类的日志对象，用以记录日志*/
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);
    private final JavaMailSender javaMailSender;
    @Autowired
    public MailClient(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String mailClient;
    /**
     * 发送邮件的方法
     * @param mailServer
     * @param subject
     * @param content
     * @return void
     * @created at 2023/9/12 14:42
    */
    public void sendMail(String mailServer, String subject, String content){
        try {
            //创建一个mimeMessage对象，此时为空
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //创建mimeMessageHelper用来帮助构建mimeMessage对象里的信息
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(mailClient);
            mimeMessageHelper.setTo(mailServer);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);
            //发送由mimeMessageHelper返回的辅助构建的mimeMessage
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败：" + e.getMessage());
        }

    }
}
