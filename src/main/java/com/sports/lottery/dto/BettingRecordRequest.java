package com.sports.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 投注记录请求DTO
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@Schema(description = "投注记录请求")
public class BettingRecordRequest {

    /**
     * 投注日期。
     */
    @Schema(description = "投注日期", required = true)
    @NotNull(message = "投注日期不能为空")
    private LocalDate betDate;

    /**
     * 运动类型，例如 football 表示足球，basketball 表示篮球。
     */
    @Schema(description = "运动类型：football-足球，basketball-篮球", required = true)
    @NotBlank(message = "运动类型不能为空")
    private String sportType;

    /**
     * 联赛名称。
     */
    @Schema(description = "联赛名称", required = true)
    @NotBlank(message = "联赛名称不能为空")
    private String league;

    /**
     * 主队名称。
     */
    @Schema(description = "主队", required = true)
    @NotBlank(message = "主队不能为空")
    private String homeTeam;

    /**
     * 客队名称。
     */
    @Schema(description = "客队", required = true)
    @NotBlank(message = "客队不能为空")
    private String awayTeam;

    /**
     * 投注类型。
     */
    @Schema(description = "投注类型", required = true)
    @NotBlank(message = "投注类型不能为空")
    private String betType;

    /**
     * 投注选项。
     */
    @Schema(description = "投注选项", required = true)
    @NotBlank(message = "投注选项不能为空")
    private String betOption;

    /**
     * 投注金额，单位为元。
     */
    @Schema(description = "投注金额（元）", required = true)
    @NotNull(message = "投注金额不能为空")
    @Positive(message = "投注金额必须大于0")
    private BigDecimal betAmount;

    /**
     * 投注赔率。
     */
    @Schema(description = "赔率", required = true)
    @NotNull(message = "赔率不能为空")
    @Positive(message = "赔率必须大于0")
    private BigDecimal odds;

    /**
     * 投注结果：1 表示中奖，2 表示未中奖。
     */
    @Schema(description = "投注结果：1-中奖，2-未中奖")
    private Integer result;

    /**
     * 实际奖金，单位为元。
     */
    @Schema(description = "实际奖金（元）")
    private BigDecimal winAmount;

    /**
     * 备注信息。
     */
    @Schema(description = "备注")
    private String notes;
}
