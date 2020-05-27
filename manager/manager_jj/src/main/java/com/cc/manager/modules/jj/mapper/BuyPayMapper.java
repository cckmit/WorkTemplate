package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.BuyPay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-08
 */
public interface BuyPayMapper extends BaseMapper<BuyPay> {

    /**
     * 通过时间段查询买量汇总数据
     *
     * @param beginTime
     * @param endTime
     * @param type
     * @return
     */
    List<BuyPay> queryByPayCollectByDate(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("type") String type);
}
