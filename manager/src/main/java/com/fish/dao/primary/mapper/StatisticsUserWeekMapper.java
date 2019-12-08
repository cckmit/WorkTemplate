package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.StatisticsUserWeek;

import java.util.List;
import java.util.Map;

public interface StatisticsUserWeekMapper
{
    int deleteByPrimaryKey(Integer newWeek);

    int insert(StatisticsUserWeek record);

    int insertSelective(StatisticsUserWeek record);

    StatisticsUserWeek selectByPrimaryKey(Integer newWeek);

    int updateByPrimaryKeySelective(StatisticsUserWeek record);

    int updateByPrimaryKey(StatisticsUserWeek record);

    List<StatisticsUserWeek> selectAll();

    List<Map> selectBySQL(String SQL);
}