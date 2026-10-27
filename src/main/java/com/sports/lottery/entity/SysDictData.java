package com.sports.lottery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_dict_data")
@Schema(description = "字典数据")
public class SysDictData {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 字典类型编码
     */
    @TableField("dict_type")
    @Schema(description = "字典类型编码")
    private String dictType;

    /**
     * 字典值
     */
    @TableField("dict_code")
    @Schema(description = "字典值")
    private String dictCode;

    /**
     * 字典标签
     */
    @TableField("dict_label")
    @Schema(description = "字典标签")
    private String dictLabel;

    /**
     * 排序
     */
    @TableField("sort_order")
    @Schema(description = "排序")
    private Integer sortOrder;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("status")
    @Schema(description = "状态：1-启用，0-停用")
    private Integer status;

    /**
     * 备注
     */
    @TableField("remark")
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
