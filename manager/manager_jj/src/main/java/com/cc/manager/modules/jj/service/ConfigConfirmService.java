package com.cc.manager.modules.jj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigConfirm;
import com.cc.manager.modules.jj.mapper.ConfigConfirmMapper;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-20
 */
@Service
public class ConfigConfirmService extends BaseCrudService<ConfigConfirm, ConfigConfirmMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigConfirm> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigConfirm> deleteWrapper) {
        return false;
    }

}
