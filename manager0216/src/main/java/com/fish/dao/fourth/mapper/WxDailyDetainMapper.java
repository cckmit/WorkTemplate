package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.WxDailyDetain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxDailyDetainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxDailyDetain record);

    int insertSelective(WxDailyDetain record);

    WxDailyDetain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxDailyDetain record);

    int updateByPrimaryKey(WxDailyDetain record);

    List<WxDailyDetain> selectAll(@Param("beginDate") int beginDate, @Param("endDate") int endDate,
                                  @Param("wxDailyDetain") WxDailyDetain wxDailyDetain);
}