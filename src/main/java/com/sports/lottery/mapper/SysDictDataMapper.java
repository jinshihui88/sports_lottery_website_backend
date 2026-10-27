package com.sports.lottery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sports.lottery.dto.DictDataResponse;
import com.sports.lottery.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    @Select("SELECT " +
            "d.dict_code AS dictCode, " +
            "d.dict_label AS dictLabel, " +
            "d.sort_order AS sortOrder " +
            "FROM sys_dict_data d " +
            "INNER JOIN sys_dict_type t " +
            "ON d.dict_type = t.dict_type " +
            "WHERE t.dict_type = #{dictType} " +
            "AND t.status = 1 " +
            "AND d.status = 1 " +
            "ORDER BY d.sort_order ASC, d.id ASC")
    List<DictDataResponse> selectEnabledDictDataByType(@Param("dictType") String dictType);
}
