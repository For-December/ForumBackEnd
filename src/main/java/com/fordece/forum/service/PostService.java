package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService extends IService<Post> {
    /**
     * 根据 id 获取贴子
     *
     * @param id 贴子 id
     * @return com.fordece.forum.entity.dto.Post
     * @author forDecember
     * @since 2023/12/20 16:52
     */
    Post getPostById(Long id);

    /**
     * 分页获取贴子，第一页有缓存（热贴）
     *
     * @param pageNum  分页号
     * @param pageSize 分页大小
     * @return java.util.List<com.fordece.forum.entity.dto.Post>
     * @author forDecember
     * @since 2023/12/20 16:52
     */
    List<Post> fetchPosts(Integer pageNum, Integer pageSize);

    /**
     * 创建贴子，内含校验，成功发贴返回 true
     *
     * @param content    必选，贴子文本内容，格式为json
     * @param images     可选，贴子图像内容
     * @param videos     可选，贴子视频内容
     * @param authorId   发贴人id
     * @param authorName 发贴人用户名
     * @param tags       贴子标签
     * @return java.lang.Boolean
     * @author forDecember
     * @since 2023/12/20 16:53
     */
    Boolean createPost(String content,
                       List<MultipartFile> images,
                       List<MultipartFile> videos,
                       Long authorId,
                       String authorName,
                       String tags);


    /**
     * 删除贴子，验证全部通过则删除贴子
     *
     * @param postId     被删除的贴子id
     * @param authorId   用于删除验证
     * @param authorName 用于删除验证
     * @return java.lang.Boolean
     * @author forDecember
     * @since 2023/12/20 17:02
     */
    Boolean deletePost(Long postId,
                       Long authorId,
                       String authorName);


    /**
     * 清除热贴缓存
     *
     * @param key 缓存的键
     * @author forDecember
     * @since 2023/12/20 16:55
     */
    void clearCache(String key);

}
