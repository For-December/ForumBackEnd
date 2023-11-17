package com.fordece.forum.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.*;
import com.plexpt.chatgpt.listener.ConsoleStreamListener;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ChatGPTUtils {

    @Getter
    private static String key;

    @Getter
    private static String endpoint;

    @Value("${openai.endpoint}")
    public void setEndpoint(String endpoint) {
        ChatGPTUtils.endpoint = endpoint;
    }

    @Value("${openai.key}")
    public void setKey(String key) {
        ChatGPTUtils.key = key;
    }

    public static Boolean check(String content) {
        System.out.println(endpoint);
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey(key)
                .apiHost(endpoint) //反向代理地址
                .build()
                .init();
        ConsoleStreamListener listener = new ConsoleStreamListener();
        Message message = Message.of("你是某校园论坛的审核员，下面引号包裹的是一则校园论坛的贴子内容，它是否符合大学生身心健康，可发布？请仅用`true`+{通过}或`false`+{你的不通过理由}回答：\n" +
                "\"" + content + "\"");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(List.of(message))
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);

        listener.setOnComplate(s -> {
            if (s.contains("true")) {
                System.out.println(s);
            } else {
                System.out.println(s.substring(s.indexOf('{')));
            }
        });
        return false;

    }
}
