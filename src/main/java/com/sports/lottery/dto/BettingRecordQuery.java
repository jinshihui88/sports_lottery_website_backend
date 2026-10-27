package com.sports.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 投注记录查询DTO
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "投注记录查询")
public class BettingRecordQuery extends PageQuery {

    /**
     * 用户ID。
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 查询开始日期。
     */
    @Schema(description = "开始日期")
    private LocalDate startDate;

    /**
     * 查询结束日期。
     */
    @Schema(description = "结束日期")
    private LocalDate endDate;

    /**
     * 运动类型，例如 football 表示足球，basketball 表示篮球。
     */
    @Schema(description = "运动类型：football-足球，basketball-篮球")
    private String sportType;

    /**
     * 联赛名称。
     */
    @Schema(description = "联赛名称")
    private String league;


    /**
     * 投注类型。
     */
    @Schema(description = "投注类型")
    private String betType;

    /**
     * 投注结果：0 表示待开奖，1 表示中奖，2 表示未中奖。
     */
    @Schema(description = "投注结果：0-待开奖，1-中奖，2-未中奖")
    private Integer result;


}
