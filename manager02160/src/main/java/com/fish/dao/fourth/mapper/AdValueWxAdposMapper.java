package com.fish.dao.fourth.mapper;

import com.fish.dao.fourth.model.AdValueWxAdpos;

import java.util.List;

public interface AdValueWxAdposMapper {

    List<AdValueWxAdpos> selectAll();

    List<AdValueWxAdpos> selectByDate(String start, String end);

    List<AdValueWxAdpos> selectTypeIncomeByDate(String start, String end,String appId);

    List<AdValueWxAdpos> selectDataCollectTypeIncome(String start, String end,String appId);
}