package com.cc.manager.modules.tt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author: CF
 * @date 2020/7/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(schema = "tt_data_analysis", value = "daily_value")
public class TtDailyValue extends BaseStatsEntity<TtDailyValue> {

    private static final long serialVersionUID = 1L;

    /**
     * 时间
     */
    @TableId("dateNum")
    private Integer dateNum;

    /**
     * appId
     */
    @TableField("appId")
    private String appId;
    /**
     * appName
     */
    @TableField(exist = false)
    private String appName;

    /**
     * app类型
     */
    @TableField("appType")
    private String appType;

    /**
     * app平台
     */
    @TableField("appPlatform")
    private String appPlatform;

    /**
     * 打开次数
     */
    @TableField("openTimes")
    private String openTimes;

    /**
     * 新增用户数
     */
    @TableField("newUsers")
    private String newUsers;

    /**
     * 活跃用户数
     */
    @TableField("activeUsers")
    private String activeUsers;

    /**
     * 数据更新时间
     */
    @TableField("insertTime")
    private LocalDateTime insertTime;

    @TableField(exist = false)
    private boolean haveDetail = false;

}
