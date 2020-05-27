package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.jj.entity.ConfigAdApp;
import com.cc.manager.modules.jj.entity.ConfigAdCombination;
import com.cc.manager.modules.jj.entity.ConfigAdStrategy;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.ConfigAdAppMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:36
 */
@Service
@DS("jj")
public class ConfigAdAppService extends BaseCrudService<ConfigAdApp, ConfigAdAppMapper> {

    private WxConfigService wxConfigService;
    private ConfigAdCombinationService configAdCombinationService;
    private ConfigAdStrategyService configAdStrategyService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdApp> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String appId = queryObject.getString("appId");
            queryWrapper.eq(StringUtils.isNotBlank(appId), "ddAppId", appId);
        }
    }

    @Override
    protected void rebuildSelectedEntity(ConfigAdApp entity) {
        entity.setAppName(this.wxConfigService.getCacheValue(WxConfig.class, entity.getAppId()));
        entity.setCombinationName(this.configAdCombinationService.getCacheValue(ConfigAdCombination.class, String.valueOf(entity.getCombinationId())));
        entity.setWxBannerStrategyName(this.configAdStrategyService.getCacheValue(ConfigAdStrategy.class, String.valueOf(entity.getWxBannerStrategyId())));
        entity.setWxIntStrategyName(this.configAdStrategyService.getCacheValue(ConfigAdStrategy.class, String.valueOf(entity.getWxIntStrategyId())));
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdApp> deleteWrapper) {
        return false;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setConfigAdCombinationService(ConfigAdCombinationService configAdCombinationService) {
        this.configAdCombinationService = configAdCombinationService;
    }

    @Autowired
    public void setConfigAdStrategyService(ConfigAdStrategyService configAdStrategyService) {
        this.configAdStrategyService = configAdStrategyService;
    }
}
