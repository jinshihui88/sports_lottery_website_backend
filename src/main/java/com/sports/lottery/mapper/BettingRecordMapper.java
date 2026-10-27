package com.sports.lottery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sports.lottery.dto.BettingRecordQuery;
import com.sports.lottery.entity.BettingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 投注记录Mapper接口
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Mapper
public interface BettingRecordMapper extends BaseMapper<BettingRecord> {
    
    /**
     * 分页查询投注记录
     * 
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<BettingRecord> selectPageByQuery(Page<BettingRecord> page, @Param("query") BettingRecordQuery query);
    
    /**
     * 统计用户投注数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Map<String, Object> selectUserStatistics(@Param("userId") Long userId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);
    
    /**
     * 按日期统计投注数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期统计数据
     */
    List<Map<String, Object>> getDailyStatistics(@Param("userId") Long userId, 
                                                @Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);
    
    /**
     * 按联赛统计投注数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 联赛统计数据
     */
    List<Map<String, Object>> getLeagueStatistics(@Param("userId") Long userId, 
                                                 @Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);
    
    /**
     * 按投注类型统计数据
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 投注类型统计数据
     */
    List<Map<String, Object>> getBetTypeStatistics(@Param("userId") Long userId, 
                                                  @Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
}