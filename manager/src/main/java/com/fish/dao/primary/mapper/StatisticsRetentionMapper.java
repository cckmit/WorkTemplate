package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.StatisticsRetention;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatisticsRetentionMapper
{
    int deleteByPrimaryKey(Date newDate);

    int insert(StatisticsRetention record);

    int insertSelective(StatisticsRetention record);

    StatisticsRetention selectByPrimaryKey(Date newDate);

    int updateByPrimaryKeySelective(StatisticsRetention record);

    int updateByPrimaryKey(StatisticsRetention record);

    List<StatisticsRetention> selectAll();

    List<Map> selectBySQL(String SQL);
}