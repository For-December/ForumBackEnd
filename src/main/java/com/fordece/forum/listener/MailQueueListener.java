package com.fordece.forum.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {

    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String, String> data) {
        String email = data.get("email");
        String code = data.get("code");
        String type = data.get("type");
        SimpleMailMessage mailMessage = switch (type) {
            case "register" -> createMessage("[sakura_forum] 欢迎注册樱花狐论坛！",
                    "您的邮件验证码为" +
                            "：" + code +
                            "\n验证码有效时间三分钟，为了您的账户安全，请勿泄露给他人", email);
            case "reset" -> createMessage("[sakura_forum] 重置密码",
                    "您正在进行重置密码操作，验证码为："
                            + code + ".非本人操作请无视", email);
            default -> null;
        };

        if (mailMessage == null) return;
        sender.send(mailMessage);

    }


    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
