package com.sports.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {

    /**
     * 用户名
     */
    @Schema(description = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}