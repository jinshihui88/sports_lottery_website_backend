package com.sports.lottery.controller;

import com.sports.lottery.common.Result;
import com.sports.lottery.dto.DictDataResponse;
import com.sports.lottery.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dict")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "字典管理", description = "字典数据查询接口")
@Slf4j
public class SysDictController {

    private final SysDictDataService sysDictDataService;

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType
     * @return Result<List < DictDataResponse>>
     */
    @GetMapping("/data")
    @Operation(summary = "根据字典类型查询字典数据")
    public Result<List<DictDataResponse>> listByDictType(@RequestParam(required = false) String dictType) {
        try {
            return Result.success(sysDictDataService.listByDictType(dictType));
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("查询字典数据失败，dictType={}", dictType, e);
            return Result.error("查询字典数据失败");
        }
    }
}
