package com.fordece.forum.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostMeta {
    private String type; // image & text & video
    private String text;
    private String url;
}
