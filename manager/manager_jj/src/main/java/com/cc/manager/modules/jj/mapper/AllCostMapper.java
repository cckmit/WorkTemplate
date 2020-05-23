package com.cc.manager.modules.jj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.manager.modules.jj.entity.AllCost;

/**
 * @author cf
 * @since 2020-05-23
 */
public interface AllCostMapper extends BaseMapper<AllCost> {

    AllCost selectCurrentCoin(String ddtime);
}
