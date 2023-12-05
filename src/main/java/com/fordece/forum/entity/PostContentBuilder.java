package com.fordece.forum.entity;

import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.List;

public class PostContentBuilder {
    private PostContentBuilder() {
    }

    private final ArrayList<PostMeta> content = new ArrayList<>();

    public static PostContentBuilder newBuilder() {
        return new PostContentBuilder();
    }

    public PostContentBuilder buildText(String text) {
        PostMeta meta = PostMeta.builder().type("text").text(text).build();
        this.content.add(meta);
        return this;
    }

    public PostContentBuilder buildImage(String image) {
        PostMeta meta = PostMeta.builder().type("image").url(image).build();
        this.content.add(meta);
        return this;
    }

    public List<PostMeta> getContent() {
        return content;
    }

    public String build() {
        return JSON.toJSONString(content);
    }


}
