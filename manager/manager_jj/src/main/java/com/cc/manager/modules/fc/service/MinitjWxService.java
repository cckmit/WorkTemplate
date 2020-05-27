package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import com.cc.manager.modules.jj.service.WxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
public class MinitjWxService extends BaseStatsService<MinitjWx, MinitjWxMapper> {


    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<MinitjWx> queryWrapper, StatsListResult statsListResult) {
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<MinitjWx> entityList, StatsListResult statsListResult) {

        return null;
    }


     List<MinitjWx> queryMinitjWxByDate(String beginTime, String endTime) {
        QueryWrapper<MinitjWx> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("DATE(wx_date)", beginTime, endTime).orderByAsc("wx_date");
        return this.list(queryWrapper);
    }

}
