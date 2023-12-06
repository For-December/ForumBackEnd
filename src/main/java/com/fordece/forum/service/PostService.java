package com.fordece.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService extends IService<Post> {
    Post getPostById(Long id);

    List<Post> fetchPosts(Integer pageNum, Integer pageSize);

    Boolean createPost(String content,
                       List<MultipartFile> images,
                       List<MultipartFile> videos,
                       Long authorId,
                       String authorName,
                       String tags);

    void clearCache(String key);

}
