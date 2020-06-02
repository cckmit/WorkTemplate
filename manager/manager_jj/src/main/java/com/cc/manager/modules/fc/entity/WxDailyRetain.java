package com.cc.manager.modules.fc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-27 20:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(schema = "persie_value", value = "wx_daily_retain")
public class WxDailyRetain extends BaseStatsEntity<WxDailyRetain> {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("appId")
    private String appId;

    @TableField("refDate")
    private String refDate;

    @TableField("dataType")
    private String dataType;

    @TableField("day0")
    private int day0;

    @TableField("day1")
    private int day1;

    @TableField("day2")
    private int day2;

    @TableField("day3")
    private int day3;

    @TableField("day4")
    private int day4;

    @TableField("day5")
    private int day5;

    @TableField("day6")
    private int day6;

    @TableField("day7")
    private int day7;

    @TableField("day14")
    private int day14;

    @TableField("day30")
    private int day30;

    @TableField(exist = false)
    private boolean haveDetail = false;
}
