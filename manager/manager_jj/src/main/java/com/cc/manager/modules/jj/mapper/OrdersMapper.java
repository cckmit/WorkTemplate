package com.cc.manager.modules.jj.mapper;

import com.cc.manager.modules.jj.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {
    /**
     * 查询实时付费统计
     *
     * @param start
     * @param end

     * @return
     */
    @Select("SELECT DATE(o.ddTrans) AS ddTrans, SUM(o.ddPrice) AS ddPrice, COUNT(DISTINCT o.ddUid) AS payUsers,o.ddAppId AS ddAppId FROM persie.orders o, persie.wx_config w where DATE(o.ddTrans) BETWEEN #{start} AND #{end} AND ddOrder !='null'\n" +
            "AND o.ddAppId = w.ddAppId GROUP BY DATE(o.ddTrans) ,o.ddAppId ORDER BY DATE(o.ddTrans) ASC")
    List<Orders> queryBuyStatistic(String start, String end);
    @Select("select * from orders where ddId = #{ddid,jdbcType=VARCHAR}")
    Orders selectByPrimaryKey(String orderId);

    void updateByPrimaryKey(Orders order);

    void updateByPrimaryKeySelective(Orders order);
}
