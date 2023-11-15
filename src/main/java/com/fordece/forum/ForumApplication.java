package com.fordece.forum;

import com.fordece.forum.utils.ChatGPTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// mybatis 还未来得及支持springboot3.2
//https://github.com/mybatis/spring/issues/855
@SpringBootApplication
public class ForumApplication {
    static final Logger logger = LoggerFactory.getLogger(ForumApplication.class);

    public static void main(String[] args) {
        System.setProperty("spring.amqp.deserialization.trust.all", "true");
        SpringApplication.run(ForumApplication.class, args);
        logger.warn("测试");
//        ChatGPTUtils.checkPost();

    }

}
