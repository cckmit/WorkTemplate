package com.cc.manager.modules.jj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author cf
 * @since 2020-05-23
 */
@Data
@TableName(schema = "persie", value = "all_cost")
public class AllCost implements BaseCrudEntity<AllCost> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    @TableField("ddUid")
    private String ddUid;

    /**
     * 产品信息
     */
    @TableField("ddAppId")
    private String ddAppId;

    /**
     * 数值类型
     */
    @TableField("ddType")
    private String ddType;

    /**
     * 变更前数据
     */
    @TableField("ddHistory")
    private Long ddHistory;

    /**
     * 变更后数据
     */
    @TableField("ddCurrent")
    private Long ddCurrent;

    /**
     * 消耗值
     */
    @TableField("ddValue")
    private Integer ddValue;

    /**
     * 操作类型
     */
    @TableField("ddCostType")
    private String ddCostType;

    /**
     * 操作附加属性
     */
    @TableField("ddCostExtra")
    private String ddCostExtra;

    /**
     * 消耗时间
     */
    @TableField("ddTime")
    private LocalDateTime ddTime;


}
