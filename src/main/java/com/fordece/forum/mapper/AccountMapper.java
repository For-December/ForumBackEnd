package com.fordece.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fordece.forum.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
