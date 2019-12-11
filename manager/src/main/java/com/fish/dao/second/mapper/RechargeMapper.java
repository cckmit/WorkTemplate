package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Recharge;

import java.util.List;

public interface RechargeMapper {
    int deleteByPrimaryKey(String ddid);

    int insert(Recharge record);

    int insertSelective(Recharge record);

    Recharge selectByPrimaryKey(String ddid);

    List<Recharge> selectAll();
    int updateByPrimaryKeySelective(Recharge record);

    int updateByPrimaryKey(Recharge record);
}