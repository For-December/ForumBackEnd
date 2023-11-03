package com.fordece.forum.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Bean("mailQueue")
    public Queue emailQueue() {
        return QueueBuilder.durable("mail").build();
    }
}
