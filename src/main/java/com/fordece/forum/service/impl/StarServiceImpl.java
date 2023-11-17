package com.fordece.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fordece.forum.entity.dto.Star;
import com.fordece.forum.mapper.StarMapper;
import com.fordece.forum.service.StarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StarServiceImpl extends ServiceImpl<StarMapper, Star> implements StarService {
}
