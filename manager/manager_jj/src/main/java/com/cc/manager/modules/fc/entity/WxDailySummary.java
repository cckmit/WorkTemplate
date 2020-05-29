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
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(schema = "persie_value", value = "wx_daily_summary")
public class WxDailySummary extends BaseStatsEntity<WxDailySummary> {

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
     * 累计用户数
     */
    @TableField("visitTotal")
    private int visitTotal;
    /**
     * 转发次数
     */
    @TableField("sharePv")
    private int sharePv;
    /**
     * 转发用户数
     */
    @TableField("shareUv")
    private int shareUv;
    /**
     * 数据插入日期
     */
    @TableField("insertTime")
    private LocalDateTime insertTime;

    @TableField(exist = false)
    private boolean haveDetail = false;

}
