package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.WxWeeklyRetain;

import java.util.List;

public interface WxWeeklyRetainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxWeeklyRetain record);

    int insertSelective(WxWeeklyRetain record);

    WxWeeklyRetain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxWeeklyRetain record);

    int updateByPrimaryKey(WxWeeklyRetain record);

    List<WxWeeklyRetain> selectAll(int beginWeek, int endWeek, WxWeeklyRetain wxWeeklyRetain);
}