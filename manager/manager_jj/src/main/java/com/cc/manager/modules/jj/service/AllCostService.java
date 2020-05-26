package com.cc.manager.modules.jj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.AllCost;
import com.cc.manager.modules.jj.mapper.AllCostMapper;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-23
 */
@Service
public class AllCostService extends BaseCrudService<AllCost, AllCostMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<AllCost> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<AllCost> deleteWrapper) {
        return false;
    }
    
}
