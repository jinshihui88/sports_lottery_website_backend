package com.sports.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录缓存DTO：存入Redis的用户登录态
 * <p>不使用User实体序列化，避免Jackson的@JsonIgnore等注解干扰，保证密码哈希等字段可靠落缓存</p>
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCache {

    /** 登录生成的JWT Token */
    private String token;

    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 密码哈希（BCrypt），仅用于缓存命中时校验密码 */
    private String password;

    /** 邮箱 */
    private String email;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 状态：0-禁用，1-启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;
}
