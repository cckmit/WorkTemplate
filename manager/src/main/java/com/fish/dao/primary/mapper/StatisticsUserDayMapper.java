package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.StatisticsUserDay;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatisticsUserDayMapper
{
    int deleteByPrimaryKey(Date newDate);

    int insert(StatisticsUserDay record);

    int insertSelective(StatisticsUserDay record);

    StatisticsUserDay selectByPrimaryKey(Date newDate);

    int updateByPrimaryKeySelective(StatisticsUserDay record);

    int updateByPrimaryKey(StatisticsUserDay record);

    List<StatisticsUserDay> selectAll();

    List<Map> selectBySQL(String SQL);
}