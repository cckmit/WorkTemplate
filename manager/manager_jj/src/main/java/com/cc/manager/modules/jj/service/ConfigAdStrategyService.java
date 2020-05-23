package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigAdStrategy;
import com.cc.manager.modules.jj.mapper.ConfigAdStrategyMapper;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:31
 */
@Service
@DS("jj")
public class ConfigAdStrategyService extends BaseCrudService<ConfigAdStrategy, ConfigAdStrategyMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdStrategy> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdStrategy> deleteWrapper) {
        return false;
    }
}
