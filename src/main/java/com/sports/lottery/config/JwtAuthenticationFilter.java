package com.sports.lottery.config;

import com.sports.lottery.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT认证过滤器
 * 
 * 从请求头中提取JWT Token，验证并设置Spring Security上下文
 * 
 * @author jinshihui
 * @since 2025年9月10日09:24:23
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        Long userId = null;

        // 提取Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(token);
                userId = jwtUtil.getUserIdFromToken(token);
                log.info("从Token中解析到用户: username={}, userId={}", username, userId);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("JWT Token解析失败: {}", e.getMessage());
            }
        }

        // 如果Token有效且当前没有认证信息，则设置认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // 验证Token是否有效
                if (jwtUtil.validateToken(token, username)) {
                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 将用户ID存储到认证对象的details中，方便后续使用
                    request.setAttribute("userId", userId);
                    request.setAttribute("username", username);
                    
                    // 设置到Security上下文
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("JWT认证成功，用户: {}", username);
                } else {
                    log.warn("JWT Token验证失败，用户: {}", username);
                }
            } catch (Exception e) {
                log.error("JWT认证过程中发生错误: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}