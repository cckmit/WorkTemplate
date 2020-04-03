package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.WxDailySummary;

import java.util.List;

public interface WxDailySummaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxDailySummary record);

    int insertSelective(WxDailySummary record);

    WxDailySummary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxDailySummary record);

    int updateByPrimaryKey(WxDailySummary record);

    List<WxDailySummary> selectAll(int beginDate, int endDate, WxDailySummary wxDailySummary);
}