package com.sports.lottery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sports.lottery.dto.BettingRecordQuery;
import com.sports.lottery.dto.BettingRecordRequest;
import com.sports.lottery.entity.BettingRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 投注记录服务接口
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
public interface BettingRecordService extends IService<BettingRecord> {
    
    /**
     * 分页查询投注记录
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<BettingRecord> getRecordPage(BettingRecordQuery query);
    
    /**
     * 添加投注记录
     * 
     * @param userId 用户ID
     * @param request 投注记录请求
     * @return 添加结果
     */
    boolean addRecord(Long userId, BettingRecordRequest request);
    
    /**
     * 批量添加投注记录
     * 
     * @param userId 用户ID
     * @param requests 投注记录请求列表
     * @return 添加结果
     */
    boolean batchAddRecords(Long userId, List<BettingRecordRequest> requests);
    
    /**
     * 更新投注记录
     * 
     * @param recordId 记录ID
     * @param request 投注记录请求
     * @return 更新结果
     */
    boolean updateRecord(Long recordId, BettingRecordRequest request);
    
    /**
     * 删除投注记录
     * 
     * @param recordId 记录ID
     * @return 删除结果
     */
    boolean deleteRecord(Long recordId);
    
    /**
     * 批量删除投注记录
     * 
     * @param recordIds 记录ID列表
     * @return 删除结果
     */
    boolean batchDeleteRecords(List<Long> recordIds);
    
    /**
     * 获取用户统计数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Map<String, Object> getUserStatistics(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取日期统计数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期统计数据
     */
    List<Map<String, Object>> getDailyStatistics(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取联赛统计数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 联赛统计数据
     */
    List<Map<String, Object>> getLeagueStatistics(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取投注类型统计数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 投注类型统计数据
     */
    List<Map<String, Object>> getBetTypeStatistics(Long userId, LocalDate startDate, LocalDate endDate);
}