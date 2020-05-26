package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.Orders;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
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
    List<Orders> queryBuyStatistic(String start, String end, String ddAppId, String productType);

    /**
     * 更新实体数据
     * @param order order
     */
    void updateByPrimaryKey(Orders order);

    /**
     * 更新非空数据
     * @param order order
     */
    void updateByPrimaryKeySelective(Orders order);

}
