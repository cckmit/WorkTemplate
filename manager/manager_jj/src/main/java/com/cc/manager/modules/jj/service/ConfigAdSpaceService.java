package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigAdSpace;
import com.cc.manager.modules.jj.entity.ConfigAdType;
import com.cc.manager.modules.jj.mapper.ConfigAdSpaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:32
 */
@Service
@DS("jj")
public class ConfigAdSpaceService extends BaseCrudService<ConfigAdSpace, ConfigAdSpaceMapper> {

    private ConfigAdTypeService configAdTypeService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdSpace> queryWrapper) {

    }

    @Override
    protected void rebuildSelectedEntity(ConfigAdSpace entity) {
        if (entity.getAdType() != 0) {
            entity.setAdTypeName(this.configAdTypeService.getCacheValue(ConfigAdType.class, String.valueOf(entity.getAdType())));
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdSpace> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setConfigAdTypeService(ConfigAdTypeService configAdTypeService) {
        this.configAdTypeService = configAdTypeService;
    }

}
