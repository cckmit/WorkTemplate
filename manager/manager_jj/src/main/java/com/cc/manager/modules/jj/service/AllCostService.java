package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.AllCost;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.mapper.AllCostMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-23
 */
@Service
public class AllCostService extends BaseStatsService<AllCost, AllCostMapper> {

    private UserInfoService userInfoService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<AllCost> queryWrapper, StatsListResult statsListResult) {

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
        String uid = statsListParam.getQueryObject().getString("uid");
        String appId = statsListParam.getQueryObject().getString("appId");
        String ddCostType = statsListParam.getQueryObject().getString("ddCostType");

        String gameCode = statsListParam.getQueryObject().getString("gameCode");
        String nickName = statsListParam.getQueryObject().getString("nickName");

        queryWrapper.between("DATE(ddTime)", beginDate, endDate);

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<AllCost> entityList, StatsListResult statsListResult) {
        for (AllCost allCost : entityList) {
            UserInfo userInfoByUuid = userInfoService.getUserInfoByUuid(allCost.getDdUid());
            allCost.setNickName(userInfoByUuid != null ? userInfoByUuid.getDdName() : "");
            String ddType = allCost.getDdType();
            if ("rmb".equals(ddType)) {
                allCost.setDdHistory(allCost.getDdHistory() / 100);
                allCost.setDdCurrent(allCost.getDdCurrent() / 100);
                allCost.setDdValue(allCost.getDdValue() / 100);
            }
        }
        return null;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 查询当前时刻用户账户金额
     *
     * @param ddTime ddTime
     * @return AllCost
     */
    public AllCost selectCurrentCoin(String ddTime) {
        QueryWrapper<AllCost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ddType", "rmb").eq(StringUtils.isNotBlank(ddTime), "ddTime", ddTime).last("LIMIT 1");
        return this.mapper.selectOne(queryWrapper);
    }

    public AllCost selectRemainAmount(String ddUid, LocalDateTime ddTimes) {
        String format = DateTimeFormatter.ofPattern("yyyyMMddHH").format(ddTimes);
        QueryWrapper<AllCost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DATE_FORMAT(ddTime,'%Y%m%d%H')", format);
        queryWrapper.eq("ddCostType", "recharge").eq(StringUtils.isNotBlank(ddUid), "ddUid", ddUid).last("LIMIT 1");
        queryWrapper.select("ddUid", "ddCurrent*0.01 AS ddCurrent", "ddTime");
        return this.mapper.selectOne(queryWrapper);
    }

}
