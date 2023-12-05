package com.fordece.forum.config;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGPTConfig {

    @Value("${openai.endpoint}")
    private String endpoint;


    @Value("${openai.key}")
    private String key;


    @Bean
    public ChatGPT chatGPT() {
        return ChatGPT.builder()
                .apiKey(key)
                .apiHost(endpoint) //反向代理地址
                .build()
                .init();
    }

    @Bean
    public ChatGPTStream chatGPTStream() {
        return ChatGPTStream.builder()
                .timeout(600)
                .apiKey(key)
                .apiHost(endpoint) //反向代理地址
                .build()
                .init();
    }
}
