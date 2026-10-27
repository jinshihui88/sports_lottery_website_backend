package com.sports.lottery.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sports.lottery.common.Result;
import com.sports.lottery.dto.BettingRecordQuery;
import com.sports.lottery.dto.BettingRecordRequest;
import com.sports.lottery.entity.BettingRecord;
import com.sports.lottery.service.BettingRecordService;
import com.sports.lottery.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 投注记录控制器
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/betting")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BettingRecordController {

    private final BettingRecordService bettingRecordService;
    private final JwtUtil jwtUtil;

    /**
     * 分页查询投注记录
     *
     * @param current 当前页码
     * @param size 每页数量
     * @param league 联赛名称
     * @param betType 投注类型
     * @param result 结果
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param request
     * @return Result<IPage < BettingRecord>> 分页查询结果
     */
    @GetMapping("/records")
    public Result<IPage<BettingRecord>> getRecords(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String league,
            @RequestParam(required = false) String betType,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("未登录");
            }

            BettingRecordQuery query = new BettingRecordQuery();
            query.setPageNum(current);
            query.setPageSize(size);
            query.setUserId(userId);
            query.setLeague(league);
            query.setBetType(betType);
            if (result != null) {
                query.setResult(Integer.valueOf(result));
            }
            query.setStartDate(startDate);
            query.setEndDate(endDate);

            IPage<BettingRecord> page = bettingRecordService.getRecordPage(query);
            return Result.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 添加投注记录
     *
     * @param request 投注记录请求参数
     * @param httpRequest
     * @return Result<String> 添加结果
     */
    @PostMapping("/records")
    public Result<String> addRecord(@Validated @RequestBody BettingRecordRequest request, HttpServletRequest httpRequest) {
        try {
            Long userId = getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return Result.error("未登录");
            }

            boolean success = bettingRecordService.addRecord(userId, request);
            if (success) {
                return Result.success("添加成功");
            } else {
                return Result.error("添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 批量添加投注记录
     *
     * @param requests
     * @param httpRequest
     * @return Result<String> 批量添加结果
     */
    @PostMapping("/records/batch")
    public Result<String> batchAddRecords(@Validated @RequestBody List<BettingRecordRequest> requests, HttpServletRequest httpRequest) {
        try {
            Long userId = getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return Result.error("未登录");
            }

            boolean success = bettingRecordService.batchAddRecords(userId, requests);
            if (success) {
                return Result.success("批量添加成功");
            } else {
                return Result.error("批量添加失败");
            }
        } catch (Exception e) {
            return Result.error("批量添加失败：" + e.getMessage());
        }
    }

    /**
     * 更新投注记录
     */
    @PutMapping("/records/{id}")
    public Result<String> updateRecord(@PathVariable Long id, @Validated @RequestBody BettingRecordRequest request,
            HttpServletRequest httpRequest) {
        try {
            Long userId = getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return Result.error("未登录");
            }

            // 验证记录是否属于当前用户
            BettingRecord record = bettingRecordService.getById(id);
            if (record == null) {
                return Result.error("记录不存在");
            }
            if (!record.getUserId().equals(userId)) {
                return Result.error("无权限操作");
            }

            boolean success = bettingRecordService.updateRecord(id, request);
            if (success) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除投注记录
     *
     * @param id 投注记录ID
     * @param request
     * @return Result<String> 删除结果
     */
    @DeleteMapping("/records/{id}")
    public Result<String> deleteRecord(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("未登录");
            }

            // 验证记录是否属于当前用户
            BettingRecord record = bettingRecordService.getById(id);
            if (record == null) {
                return Result.error("记录不存在");
            }
            if (!record.getUserId().equals(userId)) {
                return Result.error("无权限操作");
            }

            boolean success = bettingRecordService.deleteRecord(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除投注记录
     *
     * @param recordIds 要删除的投注记录ID列表
     * @param request
     * @return Result<String> 批量删除结果
     */
    @DeleteMapping("/records/batch")
    public Result<String> batchDeleteRecords(@RequestBody List<Long> recordIds, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("未登录");
            }

            // 验证所有记录是否属于当前用户
            List<BettingRecord> records = bettingRecordService.listByIds(recordIds);
            boolean hasInvalidRecord = records.stream().anyMatch(record -> !record.getUserId().equals(userId));
            if (hasInvalidRecord) {
                return Result.error("包含无权限操作的记录");
            }

            boolean success = bettingRecordService.batchDeleteRecords(recordIds);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户统计数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param request HTTP请求对象
     * @return Result<Map<String, Object>> 用户统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getUserStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("未登录");
            }

            Map<String, Object> statistics = bettingRecordService.getUserStatistics(userId, startDate, endDate);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取日期统计数据
     */
    @GetMapping("/statistics/daily")
    public Result<List<Map<String, Object>>> getDailyStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("未登录");
            }

            List<Map<String, Object>> statistics = bettingRecordService.getDailyStatistics(userId, startDate, endDate);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取日期统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取联赛统计数据
     */
    @GetMapping("/statistics/league")
    public Result<List<Map<String, Object>>> getLeagueStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("未登录");
            }

            List<Map<String, Object>> statistics = bettingRecordService.getLeagueStatistics(userId, startDate, endDate);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取联赛统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取投注类型统计数据
     */
    @GetMapping("/statistics/bettype")
    public Result<List<Map<String, Object>>> getBetTypeStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("未登录");
            }

            List<Map<String, Object>> statistics = bettingRecordService.getBetTypeStatistics(userId, startDate,
                    endDate);
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取投注类型统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 从请求中获取用户ID
     *
     @param request
     @return Long 用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                return jwtUtil.getUserIdFromToken(token);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}