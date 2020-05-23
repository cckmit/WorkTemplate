package com.cc.manager.modules.fc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.fc.entity.WxDailySummary;
import com.cc.manager.modules.fc.mapper.WxDailySummaryMapper;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
public class WxDailySummaryService extends BaseCrudService<WxDailySummary, WxDailySummaryMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxDailySummary> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxDailySummary> deleteWrapper) {
        return false;
    }

}
