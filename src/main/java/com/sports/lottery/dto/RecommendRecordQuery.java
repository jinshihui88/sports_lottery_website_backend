package com.sports.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 推荐记录查询DTO
 */
@Data
@Schema(description = "推荐记录查询")
public class RecommendRecordQuery {

    /**
     * 推荐日期，格式：yyyy-MM-dd。
     */
    @Schema(description = "推荐日期", example = "2024-01-01")
    private String recommendDate;

    /**
     * 比赛信息，支持模糊查询。
     */
    @Schema(description = "比赛信息")
    private String matchDesc;

    /**
     * 推荐人
     */
    @Schema(description = "推荐人")
    private String recommender;
}
