package com.fordece.forum.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fordece.forum.entity.dto.Post;
import com.fordece.forum.entity.vo.request.CreatePostVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public interface PostService extends IService<Post> {
    Post getPostById(Long id);

    List<Post> fetchPosts(Integer pageNum, Integer pageSize);

    Boolean createPost(String content,
                       List<MultipartFile> images,
                       Long authorId,
                       String authorName,
                       String tags);

    void clearCache(String key);

}
