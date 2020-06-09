package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.AllCost;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * @author cf
 * @since 2020-05-25
 */
@Service
public class MatchCostService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MatchCostService.class);

    private AllCostService allCostService;

    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空，并进行初始化
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        try {
            // 查询数据
            List<AllCost> matchCost = selectAll(beginDate, endDate, statsListParam);
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(matchCost)));
            statsListResult.setTotalRow(null);
            statsListResult.setCount(matchCost.size());
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    private List<AllCost> selectAll(String beginDate, String endDate, StatsListParam statsListParam) {
        // 详情查询，分组信息获取
        String operate = statsListParam.getQueryObject().getString("operate");
        // 初始化金币，视频Wrapper
        QueryWrapper<AllCost> coinWrapper = new QueryWrapper<>();
        QueryWrapper<AllCost> videoWrapper = new QueryWrapper<>();
        // 初始化查询字段列表
        List<String> coinSelectList = Lists.newArrayList("DATE(ddTime)AS localDate", " COUNT(1)AS coinCount", " COUNT(DISTINCT ddUid)AS coinTotal ");
        List<String> videoSelectList = Lists.newArrayList("DATE(ddTime)AS localDate", " COUNT(1)AS videoCount", " COUNT(DISTINCT ddUid)AS videoTotal ");
        coinWrapper.between("DATE(ddTime)", beginDate, endDate).eq("ddType", "coin");
        videoWrapper.between("DATE(ddTime)", beginDate, endDate).eq("ddType", "video");
        List<String> groupByList = Lists.newArrayList("DATE(ddTime)");

        this.updateParamByGroupType(operate, coinSelectList, videoSelectList, groupByList, coinWrapper, videoWrapper);
        // 查询时过滤的条件赋值
        String appId = statsListParam.getQueryObject().getString("appId");
        coinWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
        videoWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);

        String gameCode = statsListParam.getQueryObject().getString("gameCode");
        coinWrapper.eq(StringUtils.isNotBlank(gameCode), "ddCostExtra", gameCode);
        videoWrapper.eq(StringUtils.isNotBlank(gameCode), "ddCostExtra", gameCode);

        String costType = statsListParam.getQueryObject().getString("costType");
        if (StringUtils.isNotBlank(costType)) {
            coinWrapper.in("ddCostType", costType);
            videoWrapper.in("ddCostType", costType);
        } else {
            coinWrapper.in("ddCostType", "relive", "gameEnter");
            videoWrapper.in("ddCostType", "relive", "gameEnter");
        }
        // 获取金币数据
        coinWrapper.select(coinSelectList.toArray(new String[0]));
        coinWrapper.groupBy(groupByList.toArray(new String[0]));
        List<AllCost> coinList = this.allCostService.list(coinWrapper);
        // 获取视频数据
        videoWrapper.select(videoSelectList.toArray(new String[0]));
        videoWrapper.groupBy(groupByList.toArray(new String[0]));
        List<AllCost> videoList = this.allCostService.list(videoWrapper);
        // 汇合金币视频数据
        videoList.forEach(cost ->
        {
            for (AllCost temp : coinList) {
                if (temp.compare(cost)) {
                    temp.setVideoCount(cost.getVideoCount());
                    temp.setVideoTotal(cost.getVideoTotal());
                    break;
                }
            }
        });
        return coinList;
    }

    private void updateParamByGroupType(String operate, List<String> coinSelectList, List<String> videoSelectList, List<String> groupByList,
                                        QueryWrapper<AllCost> coinWrapper, QueryWrapper<AllCost> videoWrapper) {
        // 如果分组方式为空，默认不处理
        if (StringUtils.isNotBlank(operate)) {
            switch (operate) {
                case "app":
                    coinSelectList.add("ddAppId as appId");
                    videoSelectList.add("ddAppId as appId");
                    groupByList.add("appId");
                    break;
                case "game":
                    coinSelectList.add("ddCostExtra as gameCode");
                    videoSelectList.add("ddCostExtra as gameCode");
                    coinWrapper.gt("ddCostExtra", 1000);
                    videoWrapper.gt("ddCostExtra", 1000);
                    groupByList.add("gameCode");
                    break;
                case "app-game":
                    coinSelectList.add("ddAppId as appId");
                    coinSelectList.add("ddCostExtra as gameCode");
                    videoSelectList.add("ddAppId as appId");
                    videoSelectList.add("ddCostExtra as gameCode");
                    coinWrapper.gt("ddCostExtra", 1000);
                    videoWrapper.gt("ddCostExtra", 1000);
                    groupByList.add("appId");
                    groupByList.add("gameCode");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 初始化查询起止时间
     *
     * @param statsListParam 请求参数
     */
    private void updateBeginAndEndDate(StatsListParam statsListParam) {
        String beginDate = null, endDate = null;
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginDate = timeRangeArray[0].trim();
            endDate = timeRangeArray[1].trim();
        }
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setAllCostService(AllCostService allCostService) {
        this.allCostService = allCostService;
    }

}
