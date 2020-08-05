package com.blazefire.dao.second.mapper;

import com.blazefire.dao.second.model.WxDailySummary;
import org.springframework.stereotype.Repository;

/**
 * WxDailySummary Mapper
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 16:27
 */
@Repository
public interface WxDailySummaryMapper {

    /**
     * 存储小程序数据概况
     *
     * @param wxDailySummary 小程序数据概况
     * @return id
     */
    int insert(WxDailySummary wxDailySummary);

    /**
     * 删除小程序数据概况
     *
     * @param wxDailySummary 小程序数据概况
     * @return 删除结果
     */
    int delete(WxDailySummary wxDailySummary);

}
