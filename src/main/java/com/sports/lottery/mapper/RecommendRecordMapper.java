package com.sports.lottery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sports.lottery.entity.RecommendRecord;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendRecordMapper extends BaseMapper<RecommendRecord> {

    @Select("SELECT DISTINCT recommender FROM recommend_record WHERE deleted = 0 AND recommender != ''")
    List<String> selectDistinctRecommenders();
}
