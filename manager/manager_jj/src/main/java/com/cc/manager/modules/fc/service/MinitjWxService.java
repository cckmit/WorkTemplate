package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.mapper.MinitjWxMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("fc")
public class MinitjWxService extends BaseStatsService<MinitjWx, MinitjWxMapper> {

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<MinitjWx> queryWrapper, StatsListResult statsListResult) {
    }

    public List<MinitjWx> list(String appId, String beginTime, String endTime) {
        QueryWrapper<MinitjWx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(appId), "wx_appid", appId);
        queryWrapper.between("wx_date", beginTime, endTime);
        return this.list(queryWrapper);
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<MinitjWx> entityList, StatsListResult statsListResult) {
        return null;
    }

}
