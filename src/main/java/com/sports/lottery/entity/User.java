package com.sports.lottery.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@Schema(description = "用户实体")
public class User {

    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    @JsonIgnore
    private String password;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "昵称")
    @TableField("nickname")
    private String nickname;

    /*@Schema(description = "手机号")
    @TableField("phone")
    private String phone;*/

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "状态：0-禁用，1-启用")
    @TableField("status")
    private Integer status;

    /*@Schema(description = "默认投注金额")
    @TableField("default_bet_amount")
    private Integer defaultBetAmount;

    @Schema(description = "常用联赛（JSON格式）")
    @TableField("favorite_leagues")
    private String favoriteLeagues;

    @Schema(description = "通知设置（JSON格式）")
    @TableField("notification_settings")
    private String notificationSettings;*/

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    @TableField(value = "last_login_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;
}