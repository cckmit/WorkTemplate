package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.OnlineStatistics;

import java.util.List;

public interface OnlineStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OnlineStatistics record);

    int insertSelective(OnlineStatistics record);

    OnlineStatistics selectByPrimaryKey(Integer id);

    List<OnlineStatistics> selectAll();

    int updateByPrimaryKeySelective(OnlineStatistics record);

    int updateByPrimaryKey(OnlineStatistics record);
}