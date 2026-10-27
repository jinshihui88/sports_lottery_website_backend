package com.sports.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "字典数据响应")
public class DictDataResponse {

    @Schema(description = "字典值")
    private String dictCode;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "排序")
    private Integer sortOrder;
}
