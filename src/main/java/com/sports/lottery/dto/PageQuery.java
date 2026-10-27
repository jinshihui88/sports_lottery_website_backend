package com.sports.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询基类
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@Schema(description = "分页查询")
public class PageQuery {

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "排序方向：asc-升序，desc-降序")
    private String orderDirection = "desc";
}