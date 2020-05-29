package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.WxDailyVisitTrend;
import com.cc.manager.modules.fc.mapper.WxDailyVisitTrendMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-14
 */
@Service
@DS("fc")
public class WxDailyVisitTrendService extends BaseStatsService<WxDailyVisitTrend, WxDailyVisitTrendMapper> {

    /**
     * 通过appId和起始时间
     *
     * @param appId     appId
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 微信日活数据
     */
    public List<WxDailyVisitTrend> list(String appId, String beginDate, String endDate) {
        QueryWrapper<WxDailyVisitTrend> queryWrapper = new QueryWrapper<>();
        // 当前数据存储的日期为yyyyMMdd格式，统一进行格式化处理
        queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId).
                between("refDate", StringUtils.replace(beginDate, "-", ""),
                        StringUtils.replace(endDate, "-", ""));
        return this.list(queryWrapper);
    }

    /**
     * 通过appId和起始时间
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 微信日活数据
     */
    public List<WxDailyVisitTrend> sumList(String beginDate, String endDate) {
        QueryWrapper<WxDailyVisitTrend> queryWrapper = new QueryWrapper<>();
        // 当前数据存储的日期为yyyyMMdd格式，统一进行格式化处理
        queryWrapper.between("refDate", StringUtils.replace(beginDate, "-", ""),
                StringUtils.replace(endDate, "-", "")).groupBy("refDate");
        List<String> selectList = Lists.newArrayList("refDate", "count(appId) as productCount", "sum(visitPv) as visitPv",
                "sum(visitUv) as visitUv", "sum(visitUvNew) as visitUvNew",
                "sum(stayTimeUv) as stayTimeUv");
        // 将查询字段和分组字段赋值给查询条件
        queryWrapper.select(selectList.toArray(new String[0]));
        return this.list(queryWrapper);
    }

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<WxDailyVisitTrend> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<WxDailyVisitTrend> entityList, StatsListResult statsListResult) {
        return null;
    }

}
