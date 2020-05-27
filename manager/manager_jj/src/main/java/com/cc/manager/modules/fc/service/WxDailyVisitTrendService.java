package com.cc.manager.modules.fc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.entity.WxDailyVisitTrend;
import com.cc.manager.modules.fc.mapper.WxDailyVisitTrendMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-14
 */
@Service
public class WxDailyVisitTrendService extends BaseCrudService<WxDailyVisitTrend, WxDailyVisitTrendMapper> {

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<WxDailyVisitTrend> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<WxDailyVisitTrend> deleteWrapper) {
        return false;
    }

     List<MinitjWx> selectVisitTrendSummary(String start, String end) {
        return this.mapper.selectVisitTrendSummary(start,end);

    }
}
