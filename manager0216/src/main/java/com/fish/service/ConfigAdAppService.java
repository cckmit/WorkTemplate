package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdAppMapper;
import com.fish.dao.second.model.ConfigAdApp;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 19:07
 */
@Service
public class ConfigAdAppService implements BaseService<ConfigAdApp> {

    @Autowired
    ConfigAdAppMapper adAppMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    BaseConfig baseConfig;

    @Override
    public void setDefaultSort(GetParameter parameter) {
    }

    @Override
    public Class getClassInfo() {
        return ConfigAdApp.class;
    }

    @Override
    public boolean removeIf(ConfigAdApp configAdContent, JSONObject searchData) {
        if (existValueFalse(searchData.getString("productsName"), configAdContent.getDdAppId())) {
            return true;
        }
        return (existValueFalse(searchData.getString("appId"), configAdContent.getDdAppId()));
    }

    @Override
    public List<ConfigAdApp> selectAll(GetParameter parameter) {
        List<ConfigAdApp> list = this.adAppMapper.selectAll();
        for (ConfigAdApp configAdApp : list) {
            configAdApp.setAppName(this.cacheService.getWxConfig(configAdApp.getDdAppId()).getProductName());
            configAdApp.setCombinationName(
                    configAdApp.getDdCombinationId() + "-" + this.cacheService.getConfigAdCombination(
                            configAdApp.getDdCombinationId()).getDdName());
            configAdApp.setWxBannerStrategyName(
                    configAdApp.getDdWxBannerStrategyId() + "-" + this.cacheService.getConfigAdStrategys(
                            configAdApp.getDdWxBannerStrategyId()).getDdName());
            configAdApp.setWxIntStrategyName(
                    configAdApp.getDdWxIntStrategyId() + "-" + this.cacheService.getConfigAdStrategys(
                            configAdApp.getDdWxIntStrategyId()).getDdName());
        }
        return list;
    }

    /**
     * 新增广告内容
     *
     * @param adApp
     * @return
     */
    public PostResult insert(ConfigAdApp adApp) {
        PostResult result = new PostResult();
        int id = this.adAppMapper.insert(adApp);
        if (id <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，新增广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_app", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 修改广告内容
     *
     * @param adApp
     * @return
     */
    public PostResult update(ConfigAdApp adApp) {
        PostResult result = new PostResult();
        int update = this.adAppMapper.update(adApp);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_app", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 根据ID删除广告内容
     *
     * @param deleteIds
     * @return
     */
    public PostResult delete(String deleteIds) {
        PostResult result = new PostResult();
        int delete = this.adAppMapper.delete(deleteIds);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_app", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 通过ID查询微信广告配置
     *
     * @param id 广告位置ID
     * @return 广告位置配置
     */
    public ConfigAdApp getConfigAdApp(int id) {
        if (id == 0) {
            return null;
        }
        return this.adAppMapper.select(id);
    }

}
