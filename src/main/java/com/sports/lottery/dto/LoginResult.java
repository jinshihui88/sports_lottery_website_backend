package com.sports.lottery.dto;

import com.sports.lottery.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果DTO
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {

    /** 登录生成的JWT Token */
    private String token;

    /** 登录用户信息（含密码哈希，用于登录缓存命中时校验密码） */
    private User user;
}
