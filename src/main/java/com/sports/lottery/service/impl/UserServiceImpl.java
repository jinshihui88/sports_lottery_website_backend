package com.sports.lottery.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sports.lottery.dto.LoginCache;
import com.sports.lottery.dto.LoginRequest;
import com.sports.lottery.dto.LoginResult;
import com.sports.lottery.dto.RegisterRequest;
import com.sports.lottery.entity.User;
import com.sports.lottery.mapper.UserMapper;
import com.sports.lottery.service.UserService;
import com.sports.lottery.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /** 登录缓存Key前缀 */
    private static final String LOGIN_CACHE_PREFIX = "login:user:";

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    /** 登录缓存过期时间，与JWT有效期保持一致（秒） */
    @Value("${jwt.expiration:86400}")
    private Long cacheExpiration;
    
    @Override
    public boolean register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (getUserByUsername(request.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        LambdaQueryWrapper<User> emailQuery = new LambdaQueryWrapper<>();
        emailQuery.eq(User::getEmail, request.getEmail());
        if (getOne(emailQuery) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setStatus(1); // 正常状态
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        return save(user);
    }
    
    @Override
    public LoginResult login(LoginRequest request) {
        String cacheKey = buildLoginCacheKey(request.getUsername());
        // 优先从Redis缓存读取登录态，命中时直接返回缓存的Token和用户信息
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return loginFromCache(request, cacheKey, cached);
        }
        return loginFromDb(request, cacheKey);
    }

    /**
     * 从Redis缓存读取登录结果：校验密码与账户状态后直接复用缓存的Token
     */
    private LoginResult loginFromCache(LoginRequest request, String cacheKey, String cached) {
        LoginCache cache = JSON.parseObject(cached, LoginCache.class);

        // 缓存命中仍需校验密码，防止绕过认证直接登录
        if (!passwordEncoder.matches(request.getPassword(), cache.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 通过带状态条件的更新同时校验账户状态：账户被禁用或删除时更新失败，回落到数据库流程
        LocalDateTime now = LocalDateTime.now();
        User update = new User();
        update.setLastLoginTime(now);
        boolean updated = update(update, new LambdaQueryWrapper<User>()
                .eq(User::getId, cache.getUserId())
                .eq(User::getStatus, 1));
        if (!updated) {
            redisTemplate.delete(cacheKey);
            return loginFromDb(request, cacheKey);
        }

        // 从缓存构建用户信息返回；内存中刷新最后登录时间，本次响应返回最新值；不重写缓存，避免缓存TTL被重置后比Token更晚过期
        User user = new User();
        user.setId(cache.getUserId());
        user.setUsername(cache.getUsername());
        user.setEmail(cache.getEmail());
        user.setNickname(cache.getNickname());
        user.setAvatar(cache.getAvatar());
        user.setStatus(cache.getStatus());
        user.setCreateTime(cache.getCreateTime());
        user.setLastLoginTime(now);
        return new LoginResult(cache.getToken(), user);
    }

    /**
     * 数据库登录流程：校验用户与密码后生成新Token，并写入Redis缓存
     */
    private LoginResult loginFromDb(LoginRequest request, String cacheKey) {
        // 查询用户
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("账户已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 将Token和用户信息写入Redis缓存，过期时间与Token有效期一致
        LoginCache cache = new LoginCache(token, user.getId(), user.getUsername(), user.getPassword(),
                user.getEmail(), user.getNickname(), user.getAvatar(), user.getStatus(),
                user.getCreateTime(), user.getLastLoginTime());
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(cache),
                cacheExpiration, TimeUnit.SECONDS);
        return new LoginResult(token, user);
    }

    /**
     * 构建登录缓存Key
     */
    private String buildLoginCacheKey(String username) {
        return LOGIN_CACHE_PREFIX + username;
    }
    
    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getUsername, username);
        return getOne(query);
    }
    
    @Override
    public boolean updateUserInfo(User user) {
        user.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(user);
        if (success) {
            // 清除登录缓存，避免缓存的用户资料过期失效
            User dbUser = getById(user.getId());
            if (dbUser != null) {
                redisTemplate.delete(buildLoginCacheKey(dbUser.getUsername()));
            }
        }
        return success;
    }
    
    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        boolean success = updateById(user);
        if (success) {
            // 清除登录缓存，避免缓存中的旧密码哈希继续生效
            redisTemplate.delete(buildLoginCacheKey(user.getUsername()));
        }
        return success;
    }
}
