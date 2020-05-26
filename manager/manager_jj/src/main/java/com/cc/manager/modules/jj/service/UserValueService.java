package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.UserValue;
import com.cc.manager.modules.jj.mapper.UserValueMapper;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-14
 */
@Service
@DS("jj")
public class UserValueService extends BaseCrudService<UserValue, UserValueMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<UserValue> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<UserValue> deleteWrapper) {
        return false;
    }
}
