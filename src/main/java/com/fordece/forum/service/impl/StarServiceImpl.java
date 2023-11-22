package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Star;
import com.fordece.forum.mapper.StarMapper;
import com.fordece.forum.service.StarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StarServiceImpl extends ServiceImpl<StarMapper, Star> implements StarService {
    @Override
    public Page<Star> getStarListByPostId(Long postId) {
        return null;
    }

    @Override
    public void saveStarCountToDB() {

    }

    @Override
    public void saveStarToRedis() {

    }

    @Override
    public void unStarFromRedis() {

    }

    @Override
    public void deleteStarFromRedis() {

    }

    @Override
    public void incrementStarCount() {

    }

    @Override
    public void decrementStarCount() {

    }

    @Override
    public void saveStarsToDB() {

    }
}
