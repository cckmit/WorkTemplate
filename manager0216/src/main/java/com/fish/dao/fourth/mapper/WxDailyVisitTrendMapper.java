package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.WxDailyVisitTrend;

import java.util.List;

/**
 *WxDailyVisitTrend查询接口
 *
 * @author Host-0311
 * @date 2020-04-02 14:41:26
 */
public interface WxDailyVisitTrendMapper {

    /**
     * 查询所有数据
     *
     * @param wxDailyVisitTrend
     * @return
     */
    List<WxDailyVisitTrend> selectAll(WxDailyVisitTrend wxDailyVisitTrend);

    /**
     * 产品数据--小程序部分数据
     */
    List<WxDailyVisitTrend> selectVisitTrend(String start,String end,String appId);
}
