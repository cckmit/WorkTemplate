package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.WxDailyVisitTrend;
import com.fish.dao.third.model.ProductData;

import java.util.List;

/**
 * WxDailyVisitTrend查询接口
 *
 * @author Host-0311
 * @date 2020-04-02 14:41:26
 */
public interface WxDailyVisitTrendMapper {

    /**
     * 查询所有数据
     *
     * @param wxDailyVisitTrend 实体类
     * @return 结果集
     */
    List<WxDailyVisitTrend> selectAll(WxDailyVisitTrend wxDailyVisitTrend);

    /**
     * 产品数据--小程序部分数据 新增活跃
     *
     * @param start   查询时间
     * @param end     结束时间
     * @param ddAppId AppId
     * @return 结果集
     */
    List<ProductData> selectVisitTrend(String start, String end, String ddAppId);

    /**
     * 产品数据--小程序部分数据 新增活跃  求和
     *
     * @param start 查询时间
     * @param end   结束时间
     * @return 结果集
     */
    List<ProductData> selectVisitTrendSummary(String start, String end);
}
