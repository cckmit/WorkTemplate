package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapper {

    Orders selectByPrimaryKey(String ddid);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    List<Orders> selectAll();

    List<Orders> selectBySQL(String sql);


    Orders selectResSingle(String str);

    List<Orders> selectByTimes(@Param("start") String start, @Param("end") String end);


    List<Orders> selectDdTransByTime(String start,String end);

    List<Orders> selectAppIdByTime(String time);
    Orders selectPayByTimeAndAppId(String time,String appId);
}