package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigAdApp;
import com.cc.manager.modules.jj.mapper.ConfigAdAppMapper;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:36
 */
@Service
@DS("jj")
public class ConfigAdAppService extends BaseCrudService<ConfigAdApp, ConfigAdAppMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdApp> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdApp> deleteWrapper) {
        return false;
    }

}
