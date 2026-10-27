package com.sports.lottery.controller;

import com.sports.lottery.common.Result;
import com.sports.lottery.dto.DictDataResponse;
import com.sports.lottery.service.SysDictDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysDictControllerTest {

    @Mock
    private SysDictDataService sysDictDataService;

    private SysDictController sysDictController;

    @BeforeEach
    void setUp() {
        sysDictController = new SysDictController(sysDictDataService);
    }

    @Test
    void listByDictTypeShouldReturnBadRequestWhenDictTypeMissing() {
        when(sysDictDataService.listByDictType(null)).thenThrow(new IllegalArgumentException("字典类型不能为空"));

        Result<List<DictDataResponse>> result = sysDictController.listByDictType(null);

        assertEquals(400, result.getCode());
        assertEquals("字典类型不能为空", result.getMessage());
    }

    @Test
    void listByDictTypeShouldReturnDictDataFromService() {
        List<DictDataResponse> responses = Collections.singletonList(new DictDataResponse());
        when(sysDictDataService.listByDictType("recommend_result")).thenReturn(responses);

        Result<List<DictDataResponse>> result = sysDictController.listByDictType("recommend_result");

        assertEquals(200, result.getCode());
        assertSame(responses, result.getData());
        verify(sysDictDataService).listByDictType("recommend_result");
    }
}
