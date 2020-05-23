package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class RechargeService extends BaseCrudService<Recharge, RechargeMapper> {


    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Recharge> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Recharge> deleteWrapper) {
        return false;
    }
}
