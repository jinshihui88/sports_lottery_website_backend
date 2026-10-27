package com.sports.lottery.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 投注记录实体类
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("betting_record")
@Schema(description = "投注记录实体")
public class BettingRecord {

    /**
     * 记录ID。
     */
    @Schema(description = "记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID。
     */
    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    /**
     * 投注日期。
     */
    @Schema(description = "投注日期")
    @TableField("bet_date")
    private LocalDate betDate;

    /**
     * 联赛名称。
     */
    @Schema(description = "联赛名称")
    @TableField("league")
    private String league;

    /**
     * 主队名称。
     */
    @Schema(description = "主队")
    @TableField("home_team")
    private String homeTeam;

    /**
     * 客队名称。
     */
    @Schema(description = "客队")
    @TableField("away_team")
    private String awayTeam;

    /**
     * 投注类型，例如胜平负、让球、大小球等。
     */
    @Schema(description = "投注类型：胜平负、让球、大小球等")
    @TableField("bet_type")
    private String betType;

    /**
     * 投注选项。
     */
    @Schema(description = "投注选项")
    @TableField("bet_content")
    private String betOption;

    /**
     * 投注金额，单位为元。
     */
    @Schema(description = "投注金额（元）")
    @TableField("bet_amount")
    private BigDecimal betAmount;

    /**
     * 投注赔率。
     */
    @Schema(description = "赔率")
    @TableField("odds")
    private BigDecimal odds;

    /**
     * 投注结果：1-中奖，2-未中奖
     */
    @Schema(description = "投注结果：1-中奖，2-未中奖")
    @TableField("result")
    private Integer result;

    /**
     * 实际奖金，单位为元。
     */
    @Schema(description = "实际奖金（元）")
    @TableField("win_amount")
    private BigDecimal winAmount;

    /**
     * 盈亏金额，单位为元。
     */
    @Schema(description = "盈亏金额（元）")
    @TableField("profit_loss")
    private BigDecimal profitLoss;


    /**
     * 备注信息。
     */
    @Schema(description = "备注")
    @TableField("notes")
    private String notes;

    /**
     * 运动类型，例如 football 表示足球，basketball 表示篮球。
     */
    @Schema(description = "运动类型：football-足球，basketball-篮球")
    @TableField("sport_type")
    private String sportType;

    /**
     * 创建人。
     */
    private String createBy;

    /**
     * 创建时间。
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人。
     */
    private String updateBy;

    /**
     * 更新时间。
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0 表示未删除，1 表示已删除。
     */
    @Schema(description = "是否删除：0-未删除，1-已删除")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
