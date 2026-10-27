package com.sports.lottery.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 推荐记录实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("recommend_record")
@Schema(description = "推荐记录实体")
public class RecommendRecord {

    /**
     * 记录ID
     */
    @Schema(description = "记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    /**
     * 推荐日期
     */
    @Schema(description = "推荐日期")
    @TableField("recommend_date")
    private LocalDate recommendDate;

    /**
     * 比赛描述
     */
    @Schema(description = "比赛描述")
    @TableField("match_desc")
    private String matchDesc;

    /**
     * 推荐结果
     */
    @Schema(description = "推荐结果（如：主胜、让胜+让平等）")
    @TableField("recommendation")
    private String recommendation;

    /**
     * 推荐人
     */
    @Schema(description = "推荐人")
    @TableField("recommender")
    private String recommender;

    /**
     * 最终结果
     */
    @Schema(description = "最终结果（1：成功 0：失败 2：比赛中断）")
    @TableField("result")
    private String result;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @Schema(description = "逻辑删除：0-未删除，1-已删除")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
