package com.fish.dao.primary.mapper;

import com.fish.dao.primary.model.StatisticsPay;
import com.fish.protocols.StatisticsPayVO;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatisticsPayMapper
{
    int deleteByPrimaryKey(Date payDate);

    int insert(StatisticsPay record);

    int insertSelective(StatisticsPay record);

    StatisticsPay selectByPrimaryKey(Date payDate);

    int updateByPrimaryKeySelective(StatisticsPay record);

    int updateByPrimaryKey(StatisticsPay record);

    List<StatisticsPay> selectAll();

    List<Map> selectBySQL(String SQL);

    List<StatisticsPayVO> selectByDay();

    List<StatisticsPayVO> selectByWeek();

    List<StatisticsPayVO> selectByMonth();
}