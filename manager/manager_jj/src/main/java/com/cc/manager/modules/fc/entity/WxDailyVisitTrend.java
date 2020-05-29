package com.cc.manager.modules.fc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(schema = "persie_value", value = "wx_daily_visit_trend")
public class WxDailyVisitTrend extends BaseStatsEntity<WxDailyVisitTrend> {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    /**
     * appId
     */
    @TableField("appId")
    private String appId;
    /**
     * 日期
     */
    @TableField("refDate")
    private String refDate;
    /**
     * 打开次数
     */
    @TableField("sessionCnt")
    private int sessionCnt;
    /**
     * 访问次数
     */
    @TableField("visitPv")
    private int visitPv;
    /**
     * 访问人数
     */
    @TableField("visitUv")
    private int visitUv;
    /**
     * 新用户数
     */
    @TableField("visitUvNew")
    private int visitUvNew;
    /**
     * 人均停留时长（单位：秒）
     */
    @TableField("stayTimeUv")
    private Double stayTimeUv;
    /**
     * 次均停留时长（单位：秒）
     */
    @TableField("stayTimeSession")
    private Double stayTimeSession;
    /**
     * 平均访问深度
     */
    @TableField("visitDepth")
    private Double visitDepth;
    /**
     * 数据插入时间
     */
    @TableField("insertTime")
    private LocalDateTime insertTime;

    /**
     * 展示字段-产品总数
     */
    @TableField(exist = false)
    private int productCount;

    @TableField(exist = false)
    private boolean haveDetail = false;

}
