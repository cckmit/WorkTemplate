package com.cc.manager.modules.fc.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据汇总
 *
 * @author cf
 */
@Data
public class DataCollect implements Serializable {

    /**
     * 查询的开始和结束时间
     */
    public String beginTime;
    public String endTime;
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate wxDate;
    /*** 产品数量*/
    private Integer productCount;
    /*** 新增数量*/
    private Integer newCount;
    /*** 活跃数量*/
    private Integer activeCount;
    /*** 总收入*/
    private BigDecimal revenueCount;
    /*** 广告总收入*/
    private BigDecimal adRevenueCount;
    /*** 插屏总收入*/
    private BigDecimal screenIncomeCount = new BigDecimal(0);
    /*** 充值总收入*/
    private BigDecimal rechargeCount = new BigDecimal(0);
    /*** 视频总收入*/
    private BigDecimal videoIncomeCount;
    /*** banner总收入*/
    private BigDecimal bannerIncomeCount;
    /*** 买量支出*/
    private BigDecimal buyPay = new BigDecimal(0);
    /*** 分享人数*/
    private Integer shareUserCount;
    /*** 分享次数*/
    private Integer shareCount;
    /*** 分享率*/
    private BigDecimal shareRateCount;

}
