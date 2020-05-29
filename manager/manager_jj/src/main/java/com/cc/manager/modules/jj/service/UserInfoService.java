package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.entity.UserValue;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import com.cc.manager.modules.jj.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

    private RechargeMapper rechargeMapper;
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
        userInfoQueryWrapper.like("ddUid", uuidSet);
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
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
            try {
                // 初始化查询wrapper
                QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
                this.updateGetListWrapper(statsListParam, queryWrapper, statsListResult);
                Page<UserInfo> page = new Page<>(statsListParam.getPage(), statsListParam.getLimit());
                IPage<UserInfo> entityPages = this.page(page, queryWrapper);
                if (Objects.nonNull(entityPages)) {
                    List<UserInfo> entityList = entityPages.getRecords();
                    JSONObject totalRow = this.rebuildStatsListResult(statsListParam, entityList, statsListResult);
                    statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(entityList)));
                    statsListResult.setTotalRow(totalRow);
                    statsListResult.setCount(entityPages.getTotal());
                }
            } catch (Exception e) {
                statsListResult.setCode(1);
                statsListResult.setMsg("查询结果异常，请联系开发人员！");
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return statsListResult;
    }

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<UserInfo> queryWrapper, StatsListResult statsListResult) {

        JSONObject queryObject = JSONObject.parseObject(statsListParam.getQueryData());
        if (queryObject != null) {
            String times = queryObject.getString("registerTime");
            String ddName = queryObject.getString("ddName");
            String uid = queryObject.getString("uid");
            String ddAppId = queryObject.getString("ddAppId");
            String ddOid = queryObject.getString("ddOid");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddRegisterTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            queryWrapper.like(StringUtils.isNotBlank(ddName), "ddName", ddName);
            queryWrapper.like(StringUtils.isNotBlank(uid), "ddUid", uid);
            queryWrapper.like(StringUtils.isNotBlank(ddAppId), "ddAppId", ddAppId);
            queryWrapper.like(StringUtils.isNotBlank(ddOid), "ddOId", ddOid);
        }

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
        List<Recharge> userRecharged = rechargeMapper.selectAllUserRecharged();
        for (Recharge recharge : userRecharged) {
            rechargeMap.put(recharge.getDdUid(), recharge.getDdRmb());
        }
        return rechargeMap;
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
