package com.sports.lottery.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sports.lottery.common.Result;
import com.sports.lottery.dto.RecommendRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.sports.lottery.entity.RecommendRecord;
import com.sports.lottery.service.RecommendRecordService;
import com.sports.lottery.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 推荐记录控制器
 * 
 * 提供推荐记录的分页查询、创建、更新、删除等接口。
 * 接口均要求携带 Authorization: Bearer <token>，从 Token 中解析当前用户。
 */
@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "推荐记录", description = "推荐记录管理接口")
@Slf4j
public class RecommendRecordController {

    private final RecommendRecordService recommendRecordService;

    private final JwtUtil jwtUtil;

    /**
     * 分页查询当前登录用户的推荐记录
     * 
     * @param current       当前页码（默认 1）
     * @param size          每页条数（默认 10）
     * @param query         查询条件
     * @param request       HTTP 请求（用于读取 Authorization 头部获取用户）
     * @return Result<IPage<RecommendRecord>> 分页数据
     */
    @GetMapping("/records")
    @Operation(summary = "分页查询推荐记录", description = "按当前登录用户分页返回推荐记录列表，支持按推荐日期和比赛信息筛选")
    public Result<IPage<RecommendRecord>> page(
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") Integer size,
            @ParameterObject RecommendRecordQuery query, HttpServletRequest request) {
        try {
            String recommendDate = query.getRecommendDate();
            String matchDesc = query.getMatchDesc();
            log.info("接收到分页查询推荐记录请求，current={}, size={}, recommendDate={}, matchDesc={}", current, size, recommendDate, matchDesc);
            Long userId = getUserId(request);
            if (userId == null)
                return Result.error("未登录");
            return Result.success(recommendRecordService.pageByUserWithConditions(userId, current, size, query));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询推荐人列表
     *
     * @return Result<List < String>> 推荐人列表
     */
    @GetMapping("/recommenders")
    @Operation(summary = "查询推荐人列表", description = "查询未删除推荐记录中的去重推荐人列表")
    public Result<List<String>> listRecommenders() {
        try {
            return Result.success(recommendRecordService.listRecommenders());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 新增推荐记录
     *
     * @param record  推荐记录实体
     * @param request HTTP 请求（用于读取 Authorization 头部获取用户）
     * @return Result<String> 操作结果
     */
    @PostMapping("/records")
    @Operation(summary = "新增推荐记录", description = "创建一条新的推荐记录")
    public Result<String> create(@RequestBody RecommendRecord record, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            if (userId == null)
                return Result.error("未登录");
            record.setUserId(userId);
            record.setCreateBy(String.valueOf(userId));
            record.setCreateTime(LocalDateTime.now());
            return recommendRecordService.save(record) ? Result.success("创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新推荐记录
     * 
     * @param id      记录ID
     * @param record  推荐记录实体（允许更新基础字段）
     * @param request HTTP 请求（用于读取 Authorization 头部获取用户）
     * @return Result<String> 操作结果
     */
    @PutMapping("/records/{id}")
    @Operation(summary = "更新推荐记录", description = "根据ID更新一条推荐记录")
    public Result<String> update(@PathVariable Long id, @RequestBody RecommendRecord record, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            if (userId == null)
                return Result.error("未登录");
            RecommendRecord old = recommendRecordService.getById(id);
            if (old == null || !old.getUserId().equals(userId))
                return Result.error("无权限或记录不存在");
            record.setId(id);
            record.setUpdateBy(String.valueOf(userId));
            record.setUpdateTime(LocalDateTime.now());
            return recommendRecordService.updateById(record) ? Result.success("更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除推荐记录
     * 
     * @param id      记录ID
     * @param request HTTP 请求（用于读取 Authorization 头部获取用户）
     * @return Result<String> 操作结果
     */
    @DeleteMapping("/records/{id}")
    @Operation(summary = "删除推荐记录", description = "根据ID删除一条推荐记录")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            if (userId == null)
                return Result.error("未登录");
            RecommendRecord old = recommendRecordService.getById(id);
            if (old == null || !old.getUserId().equals(userId))
                return Result.error("无权限或记录不存在");
            return recommendRecordService.removeById(id) ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 从请求属性中获取当前登录用户ID
     * （由JWT过滤器设置）
     * 
     * @param request HTTP 请求
     * @return Long 用户ID；若未登录，返回 null
     */
    private Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId != null) {
            return (Long) userId;
        }

        // 兜底方案：直接从Token解析（不推荐，因为已经有过滤器处理）
        try {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                return jwtUtil.getUserIdFromToken(token);
            }
        } catch (Exception e) {
            log.error("解析JWT Token失败: {}", e.getMessage());
        }
        return null;
    }
}
