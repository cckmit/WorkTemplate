package com.cc.manager.modules.tt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cc.manager.common.mvc.BaseCrudEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-07-10
 */
@Data
@TableName(schema = "persie_value", value = "tt_daily_ad_value")
public class TtDailyAdValue implements BaseCrudEntity<TtDailyAdValue> {

    private static final long serialVersionUID = 1L;

    /**
     * appid
     */
    @TableId("wx_appid")
    private String wxAppId;

    /**
     * 时间
     */
    private LocalDate wxDate;

    /**
     * App平台
     */
    private String wxAppType;

    /**
     * 活跃人数
     */
    private String wxActive;

    /**
     * 视频曝光
     */
    private int wxVideoShow = 0;

    /**
     * 视频点击率
     */
    private BigDecimal wxVideoClickrate = new BigDecimal(0);

    /**
     * 视频收入
     */
    private BigDecimal wxVideoIncome = new BigDecimal(0);

    /**
     * banner曝光
     */
    private int wxBannerShow = 0;

    /**
     * banner点击率
     */
    private BigDecimal wxBannerClickrate = new BigDecimal(0);

    /**
     * banner收入
     */
    private BigDecimal wxBannerIncome = new BigDecimal(0);

    /**
     * 插屏曝光
     */
    private int wxIntShow = 0;

    /**
     * 插屏点击率
     */
    private BigDecimal wxIntClickrate = new BigDecimal(0);

    /**
     * 插屏收入
     */
    private BigDecimal wxIntIncome = new BigDecimal(0);

    @TableField("insertTime")
    private LocalDateTime insertTime;

    /**
     * 总收入
     */
    @TableField(exist = false)
    private BigDecimal revenueCount;
}
