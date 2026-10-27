package com.sports.lottery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sports.lottery.dto.BettingRecordQuery;
import com.sports.lottery.dto.BettingRecordRequest;
import com.sports.lottery.entity.BettingRecord;
import com.sports.lottery.mapper.BettingRecordMapper;
import com.sports.lottery.service.BettingRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投注记录服务实现类
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class BettingRecordServiceImpl extends ServiceImpl<BettingRecordMapper, BettingRecord>
        implements BettingRecordService {

    private final BettingRecordMapper bettingRecordMapper;

    @Override
    public IPage<BettingRecord> getRecordPage(BettingRecordQuery query) {
        Page<BettingRecord> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<BettingRecord> wrapper = new LambdaQueryWrapper<>();

        // 用户ID条件
        if (query.getUserId() != null) {
            wrapper.eq(BettingRecord::getUserId, query.getUserId());
        }

        // 日期范围条件
        /*if (query.getStartDate() != null) {
            wrapper.ge("match_date", query.getStartDate());
        }
        if (query.getEndDate() != null) {
            wrapper.le("match_date", query.getEndDate());
        }*/

        // 运动类型条件
        if (StringUtils.hasText(query.getSportType())) {
            wrapper.eq(BettingRecord::getSportType, query.getSportType());
        }

        // 联赛名称
        if (StringUtils.hasText(query.getLeague())) {
            wrapper.like(BettingRecord::getLeague, query.getLeague());
        }

        // 投注类型条件
        if (StringUtils.hasText(query.getBetType())) {
            wrapper.eq(BettingRecord::getBetType, query.getBetType());
        }

        // 投注结果结果条件
        if (query.getResult() != null) {
            wrapper.eq(BettingRecord::getResult, query.getResult());
        }
        // 按创建时间倒序
        wrapper.orderByDesc(BettingRecord::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public boolean addRecord(Long userId, BettingRecordRequest request) {
        BettingRecord record = new BettingRecord();
        record.setUserId(userId);
        record.setBetDate(request.getBetDate());
        record.setLeague(request.getLeague());
        record.setHomeTeam(request.getHomeTeam());
        record.setAwayTeam(request.getAwayTeam());
        record.setBetType(request.getBetType());
        record.setBetOption(request.getBetOption());
        record.setOdds(request.getOdds());
        record.setBetAmount(request.getBetAmount());
        record.setResult(request.getResult());

        record.setWinAmount(request.getWinAmount());
        record.setProfitLoss(request.getWinAmount());
        record.setNotes(request.getNotes());
        record.setSportType(request.getSportType());
        record.setCreateBy(String.valueOf(userId));
        return save(record);
    }

    @Override
    public boolean batchAddRecords(Long userId, List<BettingRecordRequest> requests) {
        List<BettingRecord> records = requests.stream().map(request -> {
            BettingRecord record = new BettingRecord();
            record.setUserId(userId);
            record.setBetDate(request.getBetDate());
            record.setLeague(request.getLeague());
            record.setHomeTeam(request.getHomeTeam());
            record.setAwayTeam(request.getAwayTeam());
            record.setBetType(request.getBetType());
            record.setBetOption(request.getBetOption());
            record.setOdds(request.getOdds());
            record.setBetAmount(request.getBetAmount());
            record.setResult(request.getResult());

            record.setWinAmount(request.getWinAmount());
            record.setProfitLoss(request.getWinAmount());
            record.setNotes(request.getNotes());
            record.setSportType(request.getSportType());
            return record;
        }).collect(java.util.stream.Collectors.toList());
        return saveBatch(records);
    }

    @Override
    public boolean updateRecord(Long recordId, BettingRecordRequest request) {
        BettingRecord record = getById(recordId);
        if (record == null) {
            throw new RuntimeException("投注记录不存在");
        }

        record.setBetDate(request.getBetDate());
        record.setLeague(request.getLeague());
        record.setHomeTeam(request.getHomeTeam());
        record.setAwayTeam(request.getAwayTeam());
        record.setBetType(request.getBetType());
        record.setBetOption(request.getBetOption());
        record.setOdds(request.getOdds());
        record.setBetAmount(request.getBetAmount());
        record.setResult(request.getResult());
        record.setWinAmount(request.getWinAmount());
        record.setNotes(request.getNotes());
        record.setSportType(request.getSportType());
        record.setUpdateTime(LocalDateTime.now());
        return updateById(record);
    }

    @Override
    public boolean deleteRecord(Long recordId) {
        return removeById(recordId);
    }

    @Override
    public boolean batchDeleteRecords(List<Long> recordIds) {
        return removeByIds(recordIds);
    }

    @Override
    public Map<String, Object> getUserStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<BettingRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BettingRecord::getUserId, userId);
        if (startDate != null) {
            wrapper.ge(BettingRecord::getBetDate, startDate);
        }
        if (endDate != null) {
            wrapper.le(BettingRecord::getBetDate, endDate);
        }

        List<BettingRecord> records = list(wrapper);

        Map<String, Object> statistics = new HashMap<>();

        // 总投注次数
        statistics.put("totalBets", records.size());

        // 总投注金额
        BigDecimal totalBetAmount = records.stream()
                .map(BettingRecord::getBetAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("totalBetAmount", totalBetAmount);

        // 总盈利金额
        BigDecimal totalWinAmount = records.stream()
                .map(record -> record.getWinAmount() != null ? record.getWinAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("totalWinAmount", totalWinAmount);

        // 净盈利
        BigDecimal netProfit = totalWinAmount.subtract(totalBetAmount);
        statistics.put("netProfit", netProfit);

        // 胜率统计
        long winCount = records.stream()
                .filter(record -> "WIN".equals(record.getResult()))
                .count();
        long loseCount = records.stream()
                .filter(record -> "LOSE".equals(record.getResult()))
                .count();
        long drawCount = records.stream()
                .filter(record -> "DRAW".equals(record.getResult()))
                .count();

        statistics.put("winCount", winCount);
        statistics.put("loseCount", loseCount);
        statistics.put("drawCount", drawCount);

        // 胜率
        double winRate = records.size() > 0 ? (double) winCount / records.size() * 100 : 0;
        statistics.put("winRate", Math.round(winRate * 100.0) / 100.0);
        return statistics;
    }

    @Override
    public List<Map<String, Object>> getDailyStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        return bettingRecordMapper.getDailyStatistics(userId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getLeagueStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        return bettingRecordMapper.getLeagueStatistics(userId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getBetTypeStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        return bettingRecordMapper.getBetTypeStatistics(userId, startDate, endDate);
    }

}
