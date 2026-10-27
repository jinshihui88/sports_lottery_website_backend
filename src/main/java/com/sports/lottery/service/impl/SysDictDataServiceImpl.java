package com.sports.lottery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sports.lottery.dto.DictDataResponse;
import com.sports.lottery.entity.SysDictData;
import com.sports.lottery.mapper.SysDictDataMapper;
import com.sports.lottery.service.SysDictDataService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData>
        implements SysDictDataService {

    @Override
    public List<DictDataResponse> listByDictType(String dictType) {
        if (!StringUtils.hasText(dictType)) {
            throw new IllegalArgumentException("字典类型不能为空");
        }

        return baseMapper.selectEnabledDictDataByType(dictType.trim());
    }
}
