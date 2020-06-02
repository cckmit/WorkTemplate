package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.WxDailyRetain;
import com.cc.manager.modules.fc.mapper.WxDailyRetainMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-27 20:57
 */
@Service
@DS("fc")
public class WxDailyRetainService extends BaseStatsService<WxDailyRetain, WxDailyRetainMapper> {

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<WxDailyRetain> queryWrapper, StatsListResult statsListResult) {

    }

    public List<WxDailyRetain> list(String appId, String beginDate, String endDate) {
        QueryWrapper<WxDailyRetain> queryWrapper = new QueryWrapper<>();
        // 当前数据存储的日期为yyyyMMdd格式，统一进行格式化处理
        queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId).
                between("refDate", StringUtils.replace(beginDate, "-", ""),
                        StringUtils.replace(endDate, "-", "")).eq("dataType", "visit_uv_new");
        return this.list(queryWrapper);
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<WxDailyRetain> entityList, StatsListResult statsListResult) {
        return null;
    }

}
