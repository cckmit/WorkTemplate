package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.entity.UserValue;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class UserInfoService extends BaseStatsService<UserInfo, UserInfoMapper> {

    private RechargeService rechargeService;
    private UserValueService userValueService;
    private WxConfigService wxConfigService;

    /**
     * 根据用户名模糊匹配用户
     *
     * @param userName 输入的用户名
     * @return 用户信息
     */
    public List<UserInfo> getUserInfoListByUserName(String userName) {
        if (StringUtils.isNotBlank(userName)) {
            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
            userInfoQueryWrapper.like("ddName", userName);
            return this.list(userInfoQueryWrapper);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 根据uuid列表查询用户列表
     *
     * @param uuidSet uuid列表
     * @return 用户列表
     */
    public List<UserInfo> getUserInfoListByUuidList(Set<String> uuidSet) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.in("ddUid", uuidSet);
        return this.list(userInfoQueryWrapper);
    }

    /**
     * 根据uuid查询用户信息
     *
     * @param uuid uuid列表
     * @return 用户信息
     */
    public UserInfo getUserInfoByUuid(String uuid) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("ddUid", uuid).last("LIMIT 1");
        return this.mapper.selectOne(userInfoQueryWrapper);
    }

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<UserInfo> queryWrapper, StatsListResult statsListResult) {


            String times = statsListParam.getQueryObject().getString("registerTime");
            String ddName = statsListParam.getQueryObject().getString("ddName");
            String uid = statsListParam.getQueryObject().getString("uid");
            String ddAppId = statsListParam.getQueryObject().getString("ddAppId");
            String ddOid = statsListParam.getQueryObject().getString("ddOid");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddRegisterTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            queryWrapper.like(StringUtils.isNotBlank(ddName), "ddName", ddName);
            queryWrapper.like(StringUtils.isNotBlank(uid), "ddUid", uid);
            queryWrapper.like(StringUtils.isNotBlank(ddAppId), "ddAppId", ddAppId);
            queryWrapper.like(StringUtils.isNotBlank(ddOid), "ddOId", ddOid);


    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<UserInfo> entityList, StatsListResult statsListResult) {

        //已提现金额用户的提现金额赋值
        Map<String, BigDecimal> userRechargedMap = queryUserRecharged();
        for (String userId : userRechargedMap.keySet()) {
            for (UserInfo userInfo : entityList) {
                if (userInfo.getDdUid().equals(userId)) {
                    userInfo.setCashOut(userRechargedMap.get(userId).intValue());
                }
                WxConfig cacheWxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, userInfo.getDdAppId());
                userInfo.setProductName(cacheWxConfig.getProductName());
                UserValue cacheUserValue = this.userValueService.getById(userInfo.getDdUid());
                userInfo.setDdCoinCount(cacheUserValue.getDdCoinCount());
                userInfo.setDdMoney(cacheUserValue.getDdMoney() * 0.01);
            }
        }
        return null;
    }

    /**
     * 已提现金额查询
     *
     * @return Map
     */
    private Map<String, BigDecimal> queryUserRecharged() {
        Map<String, BigDecimal> rechargeMap = new HashMap<>(10);
        List<Recharge> userRecharged = rechargeService.selectAllUserRecharged();
        for (Recharge recharge : userRecharged) {
            rechargeMap.put(recharge.getDdUid(), recharge.getDdRmb());
        }
        return rechargeMap;
    }

    @Autowired
    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @Autowired
    public void setUserValueService(UserValueService userValueService) {
        this.userValueService = userValueService;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

}
