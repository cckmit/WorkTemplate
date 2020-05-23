package com.cc.manager.modules.jj.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.WxGroupHistory;
import com.cc.manager.modules.jj.entity.WxGroupManager;
import com.cc.manager.modules.jj.mapper.WxGroupHistoryMapper;
import com.cc.manager.modules.jj.mapper.WxGroupManagerMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class WxGroupManagerService extends BaseCrudService<WxGroupManager, WxGroupManagerMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxGroupManager> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxGroupManager> deleteWrapper) {
        return false;
    }
}
