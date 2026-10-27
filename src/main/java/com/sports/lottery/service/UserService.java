package com.sports.lottery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sports.lottery.dto.LoginRequest;
import com.sports.lottery.dto.RegisterRequest;
import com.sports.lottery.entity.User;

/**
 * 用户服务接口
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     * 
     * @param request 注册请求
     * @return 注册结果
     */
    boolean register(RegisterRequest request);
    
    /**
     * 用户登录
     * 
     * @param request 登录请求
     * @return JWT Token
     */
    String login(LoginRequest request);
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);
    
    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updateUserInfo(User user);
    
    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
}