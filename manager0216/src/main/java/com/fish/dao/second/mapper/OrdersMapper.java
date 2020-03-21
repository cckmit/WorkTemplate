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

    /**
     * 查询实时付费统计
     *
     * @param start
     * @param end
     * @return
     */
    List<Orders> queryBuyStatis(@Param("start") String start, @Param("end") String end,
                                @Param("ddappid") String ddappid, @Param("payState") String payState);
}