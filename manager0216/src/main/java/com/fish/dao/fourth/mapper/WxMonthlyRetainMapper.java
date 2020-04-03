package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.WxMonthlyRetain;

import java.util.List;

public interface WxMonthlyRetainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxMonthlyRetain record);

    int insertSelective(WxMonthlyRetain record);

    WxMonthlyRetain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxMonthlyRetain record);

    int updateByPrimaryKey(WxMonthlyRetain record);

   List<WxMonthlyRetain> selectAll(int beginMonth, int endDateMonth, WxMonthlyRetain WwMonthlyRetain);
}