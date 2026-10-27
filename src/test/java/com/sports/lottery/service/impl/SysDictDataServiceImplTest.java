package com.sports.lottery.service.impl;

import com.sports.lottery.dto.DictDataResponse;
import com.sports.lottery.mapper.SysDictDataMapper;
import org.apache.ibatis.annotations.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysDictDataServiceImplTest {

    @Mock
    private SysDictDataMapper sysDictDataMapper;

    private SysDictDataServiceImpl sysDictDataService;

    @BeforeEach
    void setUp() {
        sysDictDataService = new SysDictDataServiceImpl();
        ReflectionTestUtils.setField(sysDictDataService, "baseMapper", sysDictDataMapper);
    }

    @Test
    void listByDictTypeShouldQueryEnabledTypeAndDataFromMapper() {
        DictDataResponse success = new DictDataResponse();
        success.setDictCode("1");
        success.setDictLabel("成功");
        success.setSortOrder(1);

        DictDataResponse failed = new DictDataResponse();
        failed.setDictCode("0");
        failed.setDictLabel("失败");
        failed.setSortOrder(2);

        List<DictDataResponse> responses = Arrays.asList(success, failed);
        when(sysDictDataMapper.selectEnabledDictDataByType("recommend_result")).thenReturn(responses);

        List<DictDataResponse> result = sysDictDataService.listByDictType("recommend_result");

        assertSame(responses, result);
        verify(sysDictDataMapper).selectEnabledDictDataByType("recommend_result");
    }

    @Test
    void selectEnabledDictDataByTypeShouldJoinDictTypeAndFilterEnabledRows() throws NoSuchMethodException {
        Method method = SysDictDataMapper.class.getMethod("selectEnabledDictDataByType", String.class);
        Select select = method.getAnnotation(Select.class);
        String sql = String.join(" ", select.value());

        assertTrue(sql.contains("FROM sys_dict_data d"));
        assertTrue(sql.contains("INNER JOIN sys_dict_type t"));
        assertTrue(sql.contains("ON d.dict_type = t.dict_type"));
        assertTrue(sql.contains("t.dict_type = #{dictType}"));
        assertTrue(sql.contains("t.status = 1"));
        assertTrue(sql.contains("d.status = 1"));
        assertTrue(sql.contains("ORDER BY d.sort_order ASC, d.id ASC"));
    }

    @Test
    void listByDictTypeShouldRejectBlankDictType() {
        assertThrows(IllegalArgumentException.class, () -> sysDictDataService.listByDictType(" "));

        verify(sysDictDataMapper, never()).selectEnabledDictDataByType(" ");
    }
}
