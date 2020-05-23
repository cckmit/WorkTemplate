package com.cc.manager.modules.jj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.Online;
import com.cc.manager.modules.jj.mapper.OnlineMapper;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class OnlineService extends BaseCrudService<Online, OnlineMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Online> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Online> deleteWrapper) {
        return false;
    }
}
