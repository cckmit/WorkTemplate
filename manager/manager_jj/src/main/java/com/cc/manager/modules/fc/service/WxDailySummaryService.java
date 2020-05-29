package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.entity.WxDailySummary;
import com.cc.manager.modules.fc.mapper.WxDailySummaryMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-22
 */
@Service
@DS("fc")
public class WxDailySummaryService extends BaseStatsService<WxDailySummary, WxDailySummaryMapper> {

    /**
     * 通过appId和起始时间
     *
     * @param appId     appId
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 微信日活数据
     */
    public List<WxDailySummary> list(String appId, String beginDate, String endDate) {
        QueryWrapper<WxDailySummary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(appId), "appId", appId).
                between("refDate", StringUtils.replace(beginDate, "-", ""),
                        StringUtils.replace(endDate, "-", ""));
        return this.list(queryWrapper);
    }

    /**
     * 起始时间
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 微信日活数据
     */
    public List<WxDailySummary> sumList(String beginDate, String endDate) {
        QueryWrapper<WxDailySummary> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("refDate", StringUtils.replace(beginDate, "-", ""),
                StringUtils.replace(endDate, "-", "")).groupBy("refDate");
        List<String> selectList = Lists.newArrayList("refDate", "sum(shareUv) as shareUv", "sum(sharePv) as sharePv");
        // 将查询字段和分组字段赋值给查询条件
        queryWrapper.select(selectList.toArray(new String[0]));
        return this.list(queryWrapper);
    }

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<WxDailySummary> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<WxDailySummary> entityList, StatsListResult statsListResult) {
        return null;
    }

}
