package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxVisitTrend;
import org.springframework.stereotype.Repository;

/**
 * WxVisitTrend Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:36
 */
@Repository
public interface WxVisitTrendMapper {

    /**
     * 插入小程序数据日趋势
     *
     * @param wxVisitTrend 小程序数据日趋势
     * @return id
     */
    int insertDaily(WxVisitTrend wxVisitTrend);

    /**
     * 插入小程序数据月趋势
     *
     * @param wxVisitTrend 小程序数据月趋势
     * @return id
     */
    int insertMonthly(WxVisitTrend wxVisitTrend);

    /**
     * 插入小程序数据周趋势
     *
     * @param wxVisitTrend 小程序数据周趋势
     * @return id
     */
    int insertWeekly(WxVisitTrend wxVisitTrend);

    /**
     * 删除小程序数据日趋势
     *
     * @param wxVisitTrend 小程序数据日趋势
     * @return id
     */
    int deleteDaily(WxVisitTrend wxVisitTrend);

    /**
     * 删除小程序数据月趋势
     *
     * @param wxVisitTrend 小程序数据月趋势
     * @return id
     */
    int deleteMonthly(WxVisitTrend wxVisitTrend);

    /**
     * 删除小程序数据周趋势
     *
     * @param wxVisitTrend 小程序数据周趋势
     * @return id
     */
    int deleteWeekly(WxVisitTrend wxVisitTrend);

}
