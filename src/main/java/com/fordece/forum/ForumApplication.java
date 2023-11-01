package com.fordece.forum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForumApplication {
    static final Logger logger = LoggerFactory.getLogger(ForumApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
        logger.warn("测试");
    }

}
