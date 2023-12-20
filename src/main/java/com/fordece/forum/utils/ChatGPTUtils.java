package com.fordece.forum.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.*;
import com.plexpt.chatgpt.listener.ConsoleStreamListener;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class ChatGPTUtils {

    @Resource
    ChatGPTStream chatGPTStream;

    @Resource
    ChatGPT chatGPT;

    public String check(String text) {
        String ask = "你是某校园论坛的审核员，下面引号包裹的是一则校园论坛的贴子内容，它是否符合大学生身心健康，可发布？请仅用`true`+{通过}或`false`+{你的不通过理由}回答：\n" +
                "\"" + text + "\"";
        String res = chatGPT.chat(ask);
        log.info(res);
//        if (res.contains("true")) {
//            return null;
//        } else {
//            return res;
//        }
        return res.contains("true") ? null : res;
    }
    // SSE

    public Boolean streamCheck(String text) {

        ConsoleStreamListener listener = new ConsoleStreamListener();
        Message message = Message.of("你是某校园论坛的审核员，下面引号包裹的是一则校园论坛的贴子内容，它是否符合大学生身心健康，可发布？请仅用`true`+{通过}或`false`+{你的不通过理由}回答：\n" +
                "\"" + text + "\"");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(List.of(message))
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);

        listener.setOnComplate(s -> {
            if (s.contains("true")) {
                System.out.println(s);
            } else {
                System.out.println(s.substring(s.indexOf('+')));
            }
        });
        return false;

    }
}
