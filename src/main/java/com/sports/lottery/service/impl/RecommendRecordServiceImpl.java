package com.sports.lottery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sports.lottery.dto.RecommendRecordQuery;
import com.sports.lottery.entity.RecommendRecord;
import com.sports.lottery.mapper.RecommendRecordMapper;
import com.sports.lottery.service.RecommendRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendRecordServiceImpl extends ServiceImpl<RecommendRecordMapper, RecommendRecord>
        implements RecommendRecordService {

    // 使用 ServiceImpl 默认的 baseMapper 即可

    @Override
    public IPage<RecommendRecord> pageByUser(Long userId, int current, int size) {
        Page<RecommendRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<RecommendRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecommendRecord::getUserId, userId)
                .orderByDesc(RecommendRecord::getRecommendDate, RecommendRecord::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public IPage<RecommendRecord> pageByUserWithConditions(Long userId, int current, int size, RecommendRecordQuery query) {
        Page<RecommendRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<RecommendRecord> wrapper = new LambdaQueryWrapper<>();

        // 基础条件：用户ID
        wrapper.eq(RecommendRecord::getUserId, userId);
        // 推荐日期条件
        if (StringUtils.hasText(query.getRecommendDate())) {
            try {
                LocalDate date = LocalDate.parse(query.getRecommendDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                wrapper.eq(RecommendRecord::getRecommendDate, date);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }
        // 比赛信息条件（模糊查询）
        if (StringUtils.hasText(query.getMatchDesc())) {
            wrapper.like(RecommendRecord::getMatchDesc, query.getMatchDesc());
        }
        // 推荐人条件（模糊查询）
        if (StringUtils.hasText(query.getRecommender())) {
            wrapper.like(RecommendRecord::getRecommender, query.getRecommender());
        }
        // 排序：按推荐日期降序，创建时间降序
        wrapper.orderByDesc(RecommendRecord::getRecommendDate, RecommendRecord::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public List<String> listRecommenders() {
        return baseMapper.selectDistinctRecommenders();
    }
}
