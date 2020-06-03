package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.AllCost;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.AllCostMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-23
 */
@Service
public class AllCostService extends BaseStatsService<AllCost, AllCostMapper> {

    private WxConfigService wxConfigService;
    private UserInfoService userInfoService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<AllCost> queryWrapper, StatsListResult statsListResult) {
        if (StringUtils.isBlank(statsListParam.getQueryData())) {
            String nowDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
            queryWrapper.eq("ddTime", nowDate);
        } else {
            // 初始化查询的起止日期
            this.updateBeginAndEndDate(statsListParam);
            String beginDate = statsListParam.getQueryObject().getString("beginDate");
            String endDate = statsListParam.getQueryObject().getString("endDate");
            queryWrapper.between("ddTime", beginDate, endDate).orderByAsc("ddTime");
            String uid = statsListParam.getQueryObject().getString("uid");
            queryWrapper.like(StringUtils.isNotBlank(uid), "ddUid", uid);
            String appId = statsListParam.getQueryObject().getString("appId");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
            String ddCostType = statsListParam.getQueryObject().getString("ddCostType");
            queryWrapper.eq(StringUtils.isNotBlank(ddCostType), "ddCostType", ddCostType);
            String gameCode = statsListParam.getQueryObject().getString("gameCode");
            queryWrapper.eq(StringUtils.isNotBlank(gameCode), "ddCostExtra", gameCode);
            String operate = statsListParam.getQueryObject().getString("operate");
            if (StringUtils.isNotBlank(operate)) {
                if (StringUtils.equals(operate, "0")) {
                    queryWrapper.le("ddValue", 0);
                } else {
                    queryWrapper.gt("ddValue", 0);
                }
            }
            String type = statsListParam.getQueryObject().getString("ddType");
            queryWrapper.eq(StringUtils.isNotBlank(type), "ddType", type);
        }
    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<AllCost> entityList, StatsListResult statsListResult) {
        List<AllCost> newList = new ArrayList<>();
        String nickName = statsListParam.getQueryObject().getString("nickName");
        for (AllCost allCost : entityList) {
            //设置产品名称
            allCost.setAppName(this.wxConfigService.getCacheValue(WxConfig.class, allCost.getDdAppId()));
            UserInfo userInfoByUuid = userInfoService.getUserInfoByUuid(allCost.getDdUid());
            //设置用户昵称
            allCost.setNickName(userInfoByUuid != null ? userInfoByUuid.getDdName() : "");
            if (StringUtils.isNotBlank(nickName)) {
                if (!allCost.getNickName().contains(nickName)) {
                    continue;
                }
            }
            String ddType = allCost.getDdType();
            if ("rmb".equals(ddType)) {
                allCost.setDdHistory(allCost.getDdHistory() / 100);
                allCost.setDdCurrent(allCost.getDdCurrent() / 100);
                allCost.setDdValue(allCost.getDdValue() / 100);
            }
            newList.add(allCost);
        }
        entityList.clear();
        entityList.addAll(newList);

        return null;
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
            beginDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
            endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }
        statsListParam.getQueryObject().put("beginDate", beginDate + " 00:00:00");
        statsListParam.getQueryObject().put("endDate", endDate + " 23:59:59");
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

    /**
     * 获取当前剩余可提现金额
     *
     * @param ddUid   ddUid
     * @param ddTimes ddTimes
     * @return AllCost
     */
    public AllCost selectRemainAmount(String ddUid, LocalDateTime ddTimes) {
        String format = DateTimeFormatter.ofPattern("yyyyMMddHH").format(ddTimes);
        QueryWrapper<AllCost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DATE_FORMAT(ddTime,'%Y%m%d%H')", format);
        queryWrapper.eq("ddCostType", "recharge").eq(StringUtils.isNotBlank(ddUid), "ddUid", ddUid).last("LIMIT 1");
        queryWrapper.select("ddUid", "ddCurrent*0.01 AS ddCurrent", "ddTime");
        return this.mapper.selectOne(queryWrapper);
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

}
