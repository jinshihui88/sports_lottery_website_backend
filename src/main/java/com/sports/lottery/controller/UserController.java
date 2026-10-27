package com.sports.lottery.controller;

import com.alibaba.fastjson2.JSON;
import com.sports.lottery.common.Result;
import com.sports.lottery.dto.LoginRequest;
import com.sports.lottery.dto.RegisterRequest;
import com.sports.lottery.entity.User;
import com.sports.lottery.service.UserService;
import com.sports.lottery.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@Validated @RequestBody RegisterRequest request) {
        try {
            boolean success = userService.register(request);
            if (success) {
                return Result.success("注册成功");
            } else {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     * @param request
     * @return Result<Map<String, Object>> 包含token和用户信息的结果
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginRequest request) {
        try {
            log.info("用户登录请求：{}", JSON.toJSONString(request));
            String token = userService.login(request);
            User user = userService.getUserByUsername(request.getUsername());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userInfo", getUserInfo(user));
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     *
     * @param request
     * @return Result<Map < String, Object>> 包含用户信息的结果
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            if (token == null) {
                return Result.error("未登录");
            }
            
            String username = jwtUtil.getUsernameFromToken(token);
            User user = userService.getUserByUsername(username);
            
            if (user == null) {
                return Result.error("用户不存在");
            }
            return Result.success(getUserInfo(user));
        } catch (Exception e) {
            return Result.error("获取用户信息失败");
        }
    }
    
    /**
     * 更新用户信息
     *
     * @param userInfo
     * @param request
     * @return Result<String> 更新结果
     */
    @PutMapping("/info")
    public Result<String> updateUserInfo(@RequestBody User userInfo, HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            if (token == null) {
                return Result.error("未登录");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            userInfo.setId(userId);
            
            boolean success = userService.updateUserInfo(userInfo);
            if (success) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     *
     * @param passwordData
     * @param request
     * @return Result<String> 修改密码结果
     */
    @PutMapping("/password")
    public Result<String> changePassword(@RequestBody Map<String, String> passwordData, HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            if (token == null) {
                return Result.error("未登录");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 从请求中获取Token
     *
     * @param request
     * @return String token 如果存在则返回，否则返回null
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * 构建用户信息返回对象
     * @param user
     * @return Map<String, Object>
     */
    private Map<String, Object> getUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatar", user.getAvatar());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        userInfo.put("createTime", user.getCreateTime().format(formatter));
        userInfo.put("lastLoginTime", user.getLastLoginTime().format(formatter));
        return userInfo;
    }
}