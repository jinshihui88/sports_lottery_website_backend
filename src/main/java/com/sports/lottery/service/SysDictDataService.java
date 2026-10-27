package com.sports.lottery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sports.lottery.dto.DictDataResponse;
import com.sports.lottery.entity.SysDictData;

import java.util.List;

public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型编码
     * @return List<DictDataResponse>
     */
    List<DictDataResponse> listByDictType(String dictType);
}
