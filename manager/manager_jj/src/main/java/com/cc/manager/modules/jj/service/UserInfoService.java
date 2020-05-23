package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.entity.*;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import com.cc.manager.modules.jj.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class UserInfoService extends BaseCrudService<UserInfo, UserInfoMapper> {

    private  RechargeMapper rechargeMapper;
    private UserValueService userValueService;
    private WxConfigService wxConfigService;
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<UserInfo> queryWrapper) {
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String times = queryData.getString("registerTime");
            String ddName = queryData.getString("ddName");
            String uid = queryData.getString("uid");
            String ddAppId = queryData.getString("ddAppId");
            String ddOid = queryData.getString("ddOid");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(buy_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            if (StringUtils.isNotBlank(ddName)) {
                queryWrapper.like("ddName", ddName);
            }
            if (StringUtils.isNotBlank(uid)) {
                queryWrapper.like("ddUid", uid);
            }
            if (StringUtils.isNotBlank(ddAppId)) {
                queryWrapper.like("ddAppId", ddAppId);
            }
            if (StringUtils.isNotBlank(ddOid)) {
                queryWrapper.like("ddOId", ddAppId);
            }
        }
    }
    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<UserInfo> entityList) {
        //已提现金额用户的提现金额赋值
        Map<String, BigDecimal> userRechargedMap = queryUserRecharged();
        for (String userId : userRechargedMap.keySet()) {
            for (UserInfo userInfo : entityList) {
                if (userInfo.getDdUid().equals(userId)) {
                    userInfo.setCashOut(userRechargedMap.get(userId).intValue());
                }
                WxConfig cacheWxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, userInfo.getDdAppId());
                userInfo.setProductName(cacheWxConfig.getProductName());
                UserValue cacheUserValue = this.userValueService.getCacheEntity(UserValue.class, userInfo.getDdUid());
                userInfo.setDdCoinCount(cacheUserValue.getDdCoinCount());
                userInfo.setDdMoney(cacheUserValue.getDdMoney()*0.01);
            }
        }
    }
    /**
     * 已提现金额查询
     *
     * @return Map
     */
    private Map<String, BigDecimal> queryUserRecharged() {
        Map<String, BigDecimal> rechargeMap = new HashMap<>(10);
        List<Recharge> userRecharged = rechargeMapper.selectAllUserRecharged();
        for (Recharge recharge : userRecharged) {
            rechargeMap.put(recharge.getDdUid(), recharge.getDdRmb());
        }
        return rechargeMap;
    }
    @Override
    protected boolean delete(String requestParam, UpdateWrapper<UserInfo> deleteWrapper) {
        return false;
    }
    @Autowired
    public void setRechargeMapper(RechargeMapper rechargeMapper) {
        this.rechargeMapper = rechargeMapper;
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
