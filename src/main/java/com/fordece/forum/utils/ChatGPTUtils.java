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


    public static void checkPost() {
        ChatGPT chatGPT = ChatGPT.builder()
                .timeout(600)
                .apiKey(key)
                .apiHost(endpoint) //反向代理地址
                .build()
                .init();
        List<ChatFunction> functions = new ArrayList<>();
        ChatFunction function = new ChatFunction();
        function.setName("getCurrentWeather");
        function.setDescription("获取给定位置的当前天气");
        function.setParameters(ChatFunction.ChatParameter.builder()
                .type("object")
                .required(List.of("location"))
                .properties(JSON.parseObject("{\n" +
                        "          \"location\": {\n" +
                        "            \"type\": \"string\",\n" +
                        "            \"description\": \"The city and state, e.g. San Francisco, " +
                        "CA\"\n" +
                        "          },\n" +
                        "          \"unit\": {\n" +
                        "            \"type\": \"string\",\n" +
                        "            \"enum\": [\"celsius\", \"fahrenheit\"]\n" +
                        "          }\n" +
                        "        }"))
                .build());
        functions.add(function);

        Message message = Message.of("上海的天气怎么样？");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName())
                .messages(List.of(message))
                .functions(functions)
                .maxTokens(8000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message res = choice.getMessage();
        System.out.println(res);
        if ("function_call".equals(choice.getFinishReason())) {

            FunctionCallResult functionCall = res.getFunctionCall();
            String functionCallName = functionCall.getName();

            if ("getCurrentWeather".equals(functionCallName)) {
                String arguments = functionCall.getArguments();
                JSONObject jsonObject = JSON.parseObject(arguments);
                String location = jsonObject.getString("location");
                String unit = jsonObject.getString("unit");
                String weather = getCurrentWeather(location, unit);

                callWithWeather(weather, res, functions, chatGPT);
            }
        }
    }

    private static void callWithWeather(String weather, Message res, List<ChatFunction> functions, ChatGPT chatGPT) {
        Message message = Message.of("上海的天气怎么样？");
        Message function1 = Message.ofFunction(weather);
        function1.setName("getCurrentWeather");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName())
                .messages(Arrays.asList(message, res, function1))
                .functions(functions)
                .maxTokens(8000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message res2 = choice.getMessage();
        //上海目前天气晴朗，气温为 22 摄氏度。
        System.out.println(res2.getContent());
    }

    public static String getCurrentWeather(String location, String unit) {
        return "{ \"temperature\": 22, \"unit\": \"celsius\", \"description\": \"晴朗\" }";
    }

    public static String check() {
        System.out.println(endpoint);
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey(key)
                .apiHost(endpoint) //反向代理地址
                .build()
                .init();

        ConsoleStreamListener listener = new ConsoleStreamListener();
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(List.of(message))
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);

        System.out.println(message);
        return null;

    }
}
