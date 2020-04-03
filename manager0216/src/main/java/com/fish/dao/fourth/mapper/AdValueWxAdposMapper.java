package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.AdValueWxAdpos;

import java.util.List;

public interface AdValueWxAdposMapper {
    int deleteByPrimaryKey(Integer id);

    AdValueWxAdpos selectByPrimaryKey(Integer id);

    List<AdValueWxAdpos> selectAll();

    List<AdValueWxAdpos> selectByDate(String start, String end);

    List<AdValueWxAdpos> selectTypeIncomeByDate(String start, String end,String appId);
}