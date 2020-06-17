package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigAdPool;
import com.cc.manager.modules.jj.mapper.ConfigAdPoolMapper;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-06-16
 */
@Service
@DS("jj")
public class ConfigAdPoolService extends BaseCrudService<ConfigAdPool, ConfigAdPoolMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdPool> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdPool> deleteWrapper) {
        return false;
    }

}
