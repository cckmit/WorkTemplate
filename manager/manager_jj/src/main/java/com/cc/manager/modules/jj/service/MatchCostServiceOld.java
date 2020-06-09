package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.MatchCost;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author cf
 * @since 2020-05-25
 */
@Service
public class MatchCostServiceOld {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MatchCostServiceOld.class);

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

            List<MatchCost> matchCost = new ArrayList<>();
            //查询数据
            matchCost = selectAll(beginDate, endDate, statsListParam);

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

    private List<MatchCost> selectAll(String beginDate, String endDate, StatsListParam statsListParam) {

        String coinSQL = getSQL("coin", beginDate, endDate, statsListParam);
        String videoSQL = getSQL("video", beginDate, endDate, statsListParam);
        System.out.println(coinSQL);
        System.out.println(videoSQL);
        List<MatchCost> cost1 = null;
        List<MatchCost> cost2 = null;
        cost2.forEach(cost ->
        {
            for (MatchCost temp : cost1) {
                if (temp.compare(cost)) {
                    temp.setVideoCount(cost.getVideoCount());
                    temp.setVideoTotal(cost.getVideoTotal());
                    break;
                }
            }
        });
        return cost1;
    }

    private String getSQL(String operate, String start, String end, StatsListParam statsListParam) {
        Object[] pars = new Object[9];
        int index = 0;
        pars[index++] = getPar0(statsListParam);
        pars[index++] = start;
        pars[index++] = end;
        pars[index++] = getPar3(statsListParam);
        pars[index++] = operate;
        pars[index++] = getPar5(statsListParam);
        pars[index++] = getPar6(statsListParam);
        switch (operate) {
            case "coin": {
                pars[index++] = "coinCount";
                pars[index] = "coinTotal";
            }
            break;
            case "video": {
                pars[index++] = "videoCount";
                pars[index] = "videoTotal";
            }
            break;
        }
        return MessageFormat.format(getBaseSQL(), pars);
    }

    /**
     * @return 基本SQL
     */
    private String getBaseSQL() {
        return "select {0}DATE(a.`ddTime`)AS ddTime,COUNT(1)AS {7},COUNT(DISTINCT ddUid)AS {8} from persie.all_cost a where DATE(a.`ddTime`) BETWEEN \"{1}\" and \"{2}\" and ddCostType in ({3}) and ddType=\"{4}\" {6} GROUP BY DATE(ddTime){5}";
    }

    private String getPar6(StatsListParam statsListParam) {
        StringBuilder SQL = new StringBuilder();
        String gameCode = statsListParam.getQueryObject().getString("gameCode");
        if (gameCode != null && !gameCode.trim().isEmpty()) {
            SQL.append(" and ddCostExtra=").append(gameCode);
        }
        String appId = statsListParam.getQueryObject().getString("appId");
        if (appId != null && !appId.trim().isEmpty()) {
            SQL.append(" and ddAppId='").append(appId).append("'");
        }
        if (StringUtils.isNotBlank(statsListParam.getQueryObject().getString("operate"))) {
            switch (statsListParam.getQueryObject().getString("operate")) {
                case "game":
                case "app-game": {
                    SQL.append(" and ddCostExtra> 1000");
                }
                break;
            }
        }
        return SQL.toString();
    }

    private String getPar0(StatsListParam statsListParam) {
        if (StringUtils.isNotBlank(statsListParam.getQueryObject().getString("operate"))) {
            switch (statsListParam.getQueryObject().getString("operate")) {
                case "app":
                    return "ddAppId as appId,";
                case "game":
                    return "ddCostExtra as gameCode,";
                case "app-game":
                    return "ddAppId as appId,ddCostExtra as gameCode,";
                default:
                    return "";
            }
        } else {
            return "";
        }

    }

    private String getPar3(StatsListParam statsListParam) {
        Set<String> costType = new HashSet<>();
        costType.add("gameEnter");
        costType.add("relive");
        String costStr = statsListParam.getQueryObject().getString("costType");
        if (costStr != null && !costStr.trim().isEmpty()) {
            costType.removeIf(type -> !type.equals(costStr));
        }
        StringBuilder SQL = new StringBuilder();
        costType.forEach(type ->
        {
            if (0 != SQL.length()) {
                SQL.append(",");
            }
            SQL.append("'").append(type).append("'");
        });
        return SQL.toString();
    }

    private String getPar5(StatsListParam statsListParam) {
        if (StringUtils.isNotBlank(statsListParam.getQueryObject().getString("operate"))) {
            switch (statsListParam.getQueryObject().getString("operate")) {
                case "app":
                    return ",appId";
                case "game":
                    return ",gameCode";
                case "app-game":
                    return ",appId,gameCode";
                default:
                    return "";
            }
        } else {
            return "";
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
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2));
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
