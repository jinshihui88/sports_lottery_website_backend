package com.sports.lottery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sports.lottery.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);
}