package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.StatisticsUserMonth;

import java.util.List;
import java.util.Map;

public interface StatisticsUserMonthMapper
{
    int deleteByPrimaryKey(Integer newMonth);

    int insert(StatisticsUserMonth record);

    int insertSelective(StatisticsUserMonth record);

    StatisticsUserMonth selectByPrimaryKey(Integer newMonth);

    int updateByPrimaryKeySelective(StatisticsUserMonth record);

    int updateByPrimaryKey(StatisticsUserMonth record);

    List<StatisticsUserMonth> selectAll();

    List<Map> selectBySQL(String SQL);
}