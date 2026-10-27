package com.sports.lottery.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

/**
 * Spring Security配置类
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护（因为我们使用JWT）
                .csrf().disable()

                // 启用跨域支持，配合 CorsConfigurationSource 生效
                .cors()
                .and()

                // 配置会话管理为无状态
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 配置URL权限
                .authorizeRequests()
                // 预检请求直接放行
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 允许访问的公开接口
                .antMatchers(
                        "/api/user/register",
                        "/api/user/login",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/**")
                .permitAll()

                // 其他所有请求都需要认证
                .anyRequest().authenticated()

                .and()

                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 配置异常处理
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401,\"message\":\"未授权访问\",\"data\":null,\"timestamp\":"
                            + System.currentTimeMillis() + "}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":403,\"message\":\"访问被拒绝\",\"data\":null,\"timestamp\":"
                            + System.currentTimeMillis() + "}");
                });
    }
}
