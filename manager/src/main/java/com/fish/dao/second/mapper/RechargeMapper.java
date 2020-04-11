package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Recharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeMapper {

    int updateByPrimaryKeySelective(Recharge record);

    List<Recharge> selectAll();

    List<Recharge> selectAll(String start,String end);

    int selectCashOut(String dduid, String ddTime);

    List<Recharge> selectAllCharge();

    List<Recharge> selectChargedByTime(String start,String end);

    /*** 计算所有用户总的提现金额*/
    List<Recharge> selectAllUserRecharged();
}