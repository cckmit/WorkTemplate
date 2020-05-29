package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.controller.JjAndFcAppConfigService;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class RechargeService extends BaseStatsService<Recharge, RechargeMapper> {

    private UserInfoService userInfoService;
    private JjAndFcAppConfigService jjAndFcAppConfigService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Recharge> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Recharge> entityList, StatsListResult statsListResult) {
        return null;
    }

    public List<Recharge> selectAllRechargeAudit(String beginDate, String endDate) {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        // 当前数据存储的日期为yyyyMMdd格式，统一进行格式化处理
        queryWrapper.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), "DATE(ddTimes)", beginDate, "endDate");
        queryWrapper.ne("ddStatus", 200);
        // 获取街机和FC的全部app信息
        LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();

        List<Recharge> allRechargeList = this.list(queryWrapper);
        for (Recharge recharge : allRechargeList) {
            UserInfo userInfoByUuid = this.userInfoService.getUserInfoByUuid(recharge.getDdUid());
            recharge.setUserName(userInfoByUuid.getDdName());
            JSONObject appObject = getAllAppMap.get(recharge.getDdAppId());
            recharge.setProductName(appObject != null ? appObject.getString("name") : "");
            recharge.setProgramType(appObject != null ? Integer.parseInt(appObject.getString("programType")) : 0);
        }

        return allRechargeList;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

}
