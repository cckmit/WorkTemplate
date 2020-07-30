package com.cc.manager.modules.tt.entity;

import com.cc.manager.common.mvc.BaseStatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author cf
 * @since 2020-7-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtDataCollect extends BaseStatsEntity<TtDataCollect> {
    /**
     * 日期
     */
    private String wxDate;
    /*** 平台类型*/
    private String platformType = "头条";
    /*** 产品数量*/
    private Integer productCount = 0;
    /*** 新增数量*/
    private Integer newCount = 0;
    /*** 活跃数量*/
    private Integer activeCount = 0;
    /*** 总收入*/
    private BigDecimal revenueCount = new BigDecimal(0);
    /*** 广告总收入*/
    private BigDecimal adRevenueCount = new BigDecimal(0);
    /*** 插屏总收入*/
    private BigDecimal screenIncomeCount = new BigDecimal(0);
    /*** 充值总收入*/
    private BigDecimal rechargeCount = new BigDecimal(0);
    /*** 视频总收入*/
    private BigDecimal videoIncomeCount = new BigDecimal(0);
    /*** banner总收入*/
    private BigDecimal bannerIncomeCount = new BigDecimal(0);
    /*** 买量支出*/
    private BigDecimal buyPay = new BigDecimal(0);
    /*** 分享人数*/
    private Integer shareUserCount = 0;
    /*** 分享次数*/
    private Integer shareCount = 0;
    /*** 分享率*/
    private BigDecimal shareRateCount = new BigDecimal(0);

}

