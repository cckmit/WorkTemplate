package com.fish.dao.second.mapper;

import com.fish.dao.second.model.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapper {

    Orders selectByPrimaryKey(String ddid);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    List<Orders> selectAll();

    List<Orders> selectByTimes(@Param("start") String start, @Param("end") String end);


    /**
     * 查询实时付费统计
     *
     * @param start
     * @param end
     * @param ddappid
     * @param payState
     * @return
     */
    List<Orders> queryBuyStatis(String start, String end, String ddappid, String payState);

    /**
     * 小程序充值数据查询
     *
     * @return
     */
    List<Orders> queryProgramReCharge();

    /**
     * 小程序充值数据汇总查询
     *
     * @return
     */
    List<Orders> queryProgramReChargeCount();
}