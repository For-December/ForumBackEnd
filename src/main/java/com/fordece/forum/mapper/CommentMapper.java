package com.fordece.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fordece.forum.entity.dto.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
