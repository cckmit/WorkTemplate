package com.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.mapper.ConfigAdAppMapper;
import com.fish.dao.second.model.ConfigAdApp;
import com.fish.dao.second.model.ConfigAdCombination;
import com.fish.dao.second.model.ConfigAdStrategy;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PostResult;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 19:07
 */
@Service
public class ConfigAdAppService implements BaseService<ConfigAdApp> {

    @Autowired
    ConfigAdAppMapper adAppMapper;

    @Autowired
    WxConfigService wxConfigService;

    @Autowired
    BaseConfig baseConfig;

    @Autowired
    private ConfigAdStrategyService adStrategyService;

    @Autowired
    private ConfigAdCombinationService configAdCombinationService;


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
        if (existValueFalse(searchData.getString("combinationName"), configAdContent.getDdCombinationId())) {
            return true;
        }
        if (existValueFalse(searchData.getString("minVersion"), configAdContent.getDdMinVersion())) {
            return true;
        }
        return (existValueFalse(searchData.getString("appId"), configAdContent.getDdAppId()));
    }

    @Override
    public List<ConfigAdApp> selectAll(GetParameter parameter) {
        List<ConfigAdApp> list = this.adAppMapper.selectAll();
        ConcurrentHashMap<String, WxConfig> wxConfigMap = wxConfigService.getAll(WxConfig.class);
        ConcurrentHashMap<String, ConfigAdCombination> adCombinationMap = configAdCombinationService.getAll(ConfigAdCombination.class);
        for (ConfigAdApp configAdApp : list) {
            try {
                configAdApp.setAppName(wxConfigMap.get(configAdApp.getDdAppId()).getProductName());
                String combinationName = adCombinationMap.get(String.valueOf(configAdApp.getDdCombinationId())).getDdName();
                configAdApp.setCombinationName(
                        configAdApp.getDdCombinationId() + "-" + combinationName);
                configAdApp.setWxBannerStrategyName(
                        configAdApp.getDdWxBannerStrategyId() + "-" + this.adStrategyService.getEntity(
                                ConfigAdStrategy.class,
                                String.valueOf(configAdApp.getDdWxBannerStrategyId())).getDdName());
                configAdApp.setWxIntStrategyName(
                        configAdApp.getDdWxIntStrategyId() + "-" + this.adStrategyService.getEntity(
                                ConfigAdStrategy.class,
                                String.valueOf(configAdApp.getDdWxIntStrategyId())).getDdName());
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        //更新Banner、插屏、视频ID
        updateAdAppIds(adApp);

        int id = this.adAppMapper.save(adApp);
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

        //更新Banner、插屏、视频ID
        updateAdAppIds(adApp);

        int update = this.adAppMapper.save(adApp);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，修改广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_app", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 更新ids
     *
     * @param adApp
     */
    private void updateAdAppIds(ConfigAdApp adApp) {

        ConcurrentHashMap<String, WxConfig> wxConfigMap = wxConfigService.getAll(WxConfig.class);
        WxConfig wxConfig = wxConfigMap.get(adApp.getDdAppId());
        if (wxConfig != null) {
            if (StringUtils.isNotBlank(wxConfig.getBanner())) {
                if (wxConfig.getBanner().contains("-")) {
                    adApp.setDdWxBannerUnit(wxConfig.getBanner().split("-")[1]);
                } else {
                    adApp.setDdWxBannerUnit(wxConfig.getBanner());
                }
            }

            if (StringUtils.isNotBlank(wxConfig.getScreen())) {
                if (wxConfig.getBanner().contains("-")) {
                    adApp.setDdWxIntUint(wxConfig.getScreen().split("-")[1]);
                } else {
                    adApp.setDdWxIntUint(wxConfig.getScreen());
                }
            }

            if (StringUtils.isNotBlank(wxConfig.getVideo())) {
                if (wxConfig.getBanner().contains("-")) {
                    adApp.setDdWxReVideoUnit(wxConfig.getVideo().split("-")[1]);
                } else {
                    adApp.setDdWxReVideoUnit(wxConfig.getVideo());
                }
            }
        }
    }

    /**
     * 复制广告内容
     *
     * @param adApp
     * @return
     */
    public PostResult copy(ConfigAdApp adApp) {
        PostResult result = new PostResult();
        //更新Banner、插屏、视频ID
        updateAdAppIds(adApp);
        int update = this.adAppMapper.save(adApp);
        if (update <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，复制广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_app", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 根据ID删除广告内容
     *
     * @param ddAppId
     * @return
     */
    public PostResult delete(String ddAppId, String ddMinVersion) {
        PostResult result = new PostResult();
        int delete = this.adAppMapper.delete(ddAppId, ddMinVersion);
        if (delete <= 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，删除广告内容失败！");
        } else {
            ReadJsonUtil.flushTable("config_ad_app", this.baseConfig.getFlushCache());
        }
        return result;
    }

    /**
     * 通过ID查询微信广告配置
     *
     * @param key 广告位置key
     * @return 广告位置配置
     */
    public ConfigAdApp getConfigAdApp(String key) {
        String[] split;
        if (StringUtils.isEmpty(key)) {
            return null;
        } else {
            split = key.split("-");
        }
        return this.adAppMapper.select(split[0], split[1]);
    }

    /**
     * 更新页面按钮状态
     *
     * @param jsonObject
     * @return
     */
    public PostResult changeAllowedShowStatus(JSONObject jsonObject) {
        PostResult result = new PostResult();
        String id = jsonObject.getString("id");
        String[] key = id.split("-");
        Boolean allowedShow = jsonObject.getBoolean("ddAllowedShow");
        Boolean ddWxBannerAllowedShow = jsonObject.getBoolean("ddWxBannerAllowedShow");
        Boolean ddWxIntAllowedShow = jsonObject.getBoolean("ddWxIntAllowedShow");
        Boolean ddWxReVideoAllowedShow = jsonObject.getBoolean("ddWxReVideoAllowedShow");
        int count = 0;
        if (allowedShow != null) {
            count = this.adAppMapper.changeAllowedShowStatus(key[0], key[1], allowedShow);
        }

        if (ddWxBannerAllowedShow != null) {
            count = this.adAppMapper.changeBannerStatus(key[0], key[1], ddWxBannerAllowedShow);
        }

        if (ddWxIntAllowedShow != null) {
            count = this.adAppMapper.changeIconStatus(key[0], key[1], ddWxIntAllowedShow);
        }

        if (ddWxReVideoAllowedShow != null) {
            count = this.adAppMapper.changeVideoStatus(key[0], key[1], ddWxReVideoAllowedShow);
        }

        if (count <= 0) {
            result.setSuccessed(false);
            result.setMsg("更新失败");
        } else {
            ReadJsonUtil.flushTable("config_ad_app", this.baseConfig.getFlushCache());
        }
        return result;
    }

}
