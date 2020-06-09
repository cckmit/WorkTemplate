package com.cc.manager.modules.jj.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "persie", value = "all_cost")
public class AllCost extends BaseStatsEntity<AllCost> {

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("ddTime")
    private LocalDateTime ddTime;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String nickName;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String appName;

    @TableField(exist = false)
    private boolean haveDetail = false;

    /**
     * 开赛消耗展示位参数
     */
    @TableField(exist = false)
    private String appId;
    @TableField(exist = false)
    private String gameCode;
    @TableField(exist = false)
    private BigDecimal coinCount = new BigDecimal(0);
    @TableField(exist = false)
    private BigDecimal videoCount = new BigDecimal(0);
    @TableField(exist = false)
    private BigDecimal coinTotal = new BigDecimal(0);
    @TableField(exist = false)
    private BigDecimal videoTotal = new BigDecimal(0);
    @JSONField(format = "yyyy-MM-dd")
    @TableField(exist = false)
    private LocalDateTime localDate;

    /**
     * 對比是否匹配
     *
     * @param cost 消耗参数
     * @return 匹配结果
     */
    public boolean compare(AllCost cost) {
        if (getLocalDate().compareTo(cost.getLocalDate()) != 0)
            return false;
        if (appId != null && !getAppId().equals(cost.getAppId()))
            return false;
        return gameCode == null || getGameCode().equals(cost.getGameCode());
    }

}
