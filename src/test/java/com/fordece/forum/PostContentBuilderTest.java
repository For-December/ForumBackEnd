package com.fordece.forum;

import com.fordece.forum.entity.PostContentBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostContentBuilderTest {
    private static PostContentBuilder contentBuilder;

    @BeforeAll
    public static void setData() {
        contentBuilder = PostContentBuilder.newBuilder()
                .buildText("Hello")
                .buildImage("https://www.fordece.cn/")
                .buildVideo("https://video.fordece.cn/");
    }

    @Test
    public void testGetContent() {

        assertEquals(3, contentBuilder.getContent().size());
        assertEquals("text", contentBuilder.getContent().get(0).getType());
        assertEquals("Hello", contentBuilder.getContent().get(0).getText());

        assertEquals("image", contentBuilder.getContent().get(1).getType());
        assertEquals("https://www.fordece.cn/", contentBuilder.getContent().get(1).getUrl());
    }

    @Test
    public void testToJson() {


        String expectedJson = "[{\"text\":\"Hello\",\"type\":\"text\"},{\"type\":\"image\",\"url\":\"https://www.fordece.cn/\"},{\"type\":\"video\",\"url\":\"https://video.fordece.cn/\"}]";
        assertEquals(expectedJson, contentBuilder.build());
    }

    @Test
    public void testBuilderText() {

    }

    @Test
    public void testBuilderImage() {

    }

    @Test
    public void testBuilderMultiple() {
    }
}