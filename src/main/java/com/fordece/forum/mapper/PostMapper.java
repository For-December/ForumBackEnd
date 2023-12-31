package com.fordece.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fordece.forum.entity.dto.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
