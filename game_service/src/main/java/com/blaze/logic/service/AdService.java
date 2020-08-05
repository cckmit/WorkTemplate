package com.blaze.logic.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blaze.common.PostResult;
import com.blaze.common.utils.SimpleUtil;
import com.blaze.data.entity.*;
import com.blaze.data.service.*;
import com.blaze.logic.RequestConst;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 广告处理模块
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-17 17:12
 */
@Service
public class AdService {

    private static final String URL = "https://res.qinyougames.com";

    /**
     * 当前服务器支持最低版本
     */
    private static final String MIN_VERSION = "4.0.0";

    /**
     * 1-banner广告类型、3-插屏广告类型、4-icon广告类型
     */
    private static final List<String> AD_TYPE_LIST = Lists.newArrayList("1", "3", "4");


    private WxConfigService wxConfigService;
    private ConfigAdAppService configAdAppService;
    private ConfigAdContentPoolService configAdContentPoolService;
    private ConfigAdPositionService configAdPositionService;
    private ConfigAdContentService configAdContentService;
    private ConfigAdContentInfoService configAdContentInfoService;
    private WxAppConfigService wxAppConfigService;

    public PostResult get(JSONObject info) {
        PostResult postResult = new PostResult();
        // 当前服务器只支持4.0.0以上版本
        String version = info.getString(RequestConst.VERSION);
        long versionValue = SimpleUtil.convertVersion(version);
        if (versionValue < SimpleUtil.convertVersion(MIN_VERSION)) {
            postResult.updateMsg(0, "版本低于：" + MIN_VERSION);
            return postResult;
        }

        String appId = info.getString(RequestConst.APP_ID);
        String platform = info.getString(RequestConst.PLATFORM);
        // 默认平台是微信
        platform = StringUtils.isBlank(platform) ? "weixin" : platform;
        platform = StringUtils.equals("wx", platform) ? "weixin" : platform;

        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, appId);
        if (wxConfig == null) {
            postResult.updateMsg(0, "请运营检查app信息或刷新缓存！");
            return postResult;
        }

        // 获取当前App广告配置，一个App可能有多个不同版本的广告配置，获取到最合适的一个版本
        ConfigAdApp configAdApp = getConfigAdApp(version, appId, platform);

        if (configAdApp == null) {
            postResult.updateMsg(0, "无合适广告配置！");
            return postResult;
        }

        JSONObject adDataObj = new JSONObject();
        // 更新广告位置
        this.updateAdCombine(versionValue, adDataObj);
        // 更新广告内容，先获取审核版本，审核版本不下发自有广告
        WxAppConfig wxAppConfig = this.wxAppConfigService.getCacheEntity(WxAppConfig.class, appId);
        long checkVersionValue = 0L;
        if (StringUtils.isNotBlank(wxAppConfig.getCheckVersion())) {
            checkVersionValue = SimpleUtil.convertVersion(wxAppConfig.getCheckVersion());
        }
        this.updateAdAppInfo(versionValue, checkVersionValue, wxConfig, configAdApp, adDataObj);

        postResult.setData(adDataObj);
        return postResult;
    }

    /**
     * 获取合适的app广告配置
     *
     * @param version  当前版本
     * @param appId    appId
     * @param platform 平台
     * @return app广告配置
     */
    @SuppressWarnings("unchecked")
    private ConfigAdApp getConfigAdApp(String version, String appId, String platform) {
        ConfigAdApp configAdApp = null;
        Object configAdAppMapObj = this.configAdAppService.getCacheObject(ConfigAdApp.class, appId);
        if (configAdAppMapObj != null) {
            TreeMap<String, ConfigAdApp> configAdAppMap = (TreeMap<String, ConfigAdApp>) configAdAppMapObj;
            // 优先获取版本相同的配置
            configAdApp = configAdAppMap.get(version);
            // 如果没有获取到相匹配的广告，拿低于当前版本的广告
            if (configAdApp == null) {
                for (Map.Entry<String, ConfigAdApp> entry : configAdAppMap.entrySet()) {
                    if (SimpleUtil.convertVersion(version) >= SimpleUtil.convertVersion(entry.getKey())) {
                        configAdApp = entry.getValue();
                        break;
                    }
                }
            }
        }

        // 如果没有获取到合适的app配置的话，则使用平台默认配置
        if (configAdApp == null) {
            TreeMap<String, ConfigAdApp> configAdAppMap = (TreeMap<String, ConfigAdApp>) this.configAdAppService.getCacheObject(ConfigAdApp.class, "default-" + platform);
            if (configAdAppMap != null) {
                configAdApp = configAdAppMap.get("default");
            }
        }
        return configAdApp;
    }

    /**
     * 更新广告合集信息
     *
     * @param returnAdObject 广告返回值
     */
    private void updateAdCombine(long versionValue, JSONObject returnAdObject) {
        // 解析广告位置列表
        JSONArray adCombine = new JSONArray();
        ConcurrentHashMap<Serializable, ConfigAdPosition> configAdPositionCache = this.configAdPositionService.getCacheEntityMap(ConfigAdPosition.class);
        for (Map.Entry<Serializable, ConfigAdPosition> entry : configAdPositionCache.entrySet()) {
            JSONArray adPositionObject = new JSONArray();
            ConfigAdPosition configAdPosition = entry.getValue();
            if (configAdPosition.isAllowedOperation()) {
                if (versionValue >= SimpleUtil.convertVersion(configAdPosition.getMinVersion())) {
                    adPositionObject.add(entry.getKey());
                    adPositionObject.add(JSONArray.parseArray("[" + configAdPosition.getAdTypes() + "]"));
                    adCombine.add(adPositionObject);
                }
            }
        }
        returnAdObject.put("adCombine", adCombine);
    }


    /**
     * 更新广告返回值
     *
     * @param versionValue      当前版本
     * @param checkVersionValue 审核版本
     * @param wxConfig          app配置
     * @param configAdApp       app广告配置
     * @param returnAdObject    广告返回值
     */
    private void updateAdAppInfo(long versionValue, long checkVersionValue, WxConfig wxConfig, ConfigAdApp configAdApp, JSONObject returnAdObject) {
        // 广告池
        JSONObject adPool = new JSONObject();
        adPool.put("url", URL);
        // 广告内容信息
        Map<String, JSONArray> appInfoArray = new HashMap<>(16);

        ConfigAdContentPool configAdContentPool = this.configAdContentPoolService.getCacheEntity(ConfigAdContentPool.class, configAdApp.getContentPoolId());

        // 更新全部广告内容
        for (String adType : AD_TYPE_LIST) {
            this.getAdContentArray(versionValue, checkVersionValue, wxConfig, configAdApp, configAdContentPool, adType, adPool, appInfoArray);
        }

        returnAdObject.put("adPool", adPool);
        returnAdObject.put("appInfo", appInfoArray);
    }

    /**
     * 更新广告池及广告内容信息
     *
     * @param versionValue        当前版本
     * @param checkVersionValue   审核版本
     * @param wxConfig            app配置
     * @param configAdApp         app广告配置
     * @param configAdContentPool 广告内容ID池
     * @param adType              广告类型
     * @param adPool              广告内容池
     * @param appInfoArray        广告信息map
     */
    private void getAdContentArray(long versionValue, long checkVersionValue, WxConfig wxConfig, ConfigAdApp configAdApp, ConfigAdContentPool configAdContentPool, String adType, JSONObject adPool, Map<String, JSONArray> appInfoArray) {
        JSONArray adContentPoolArray = new JSONArray();
        JSONArray adContentIdArray = JSONObject.parseObject(configAdContentPool.getContentIds()).getJSONArray(adType);

        for (int i = 0; i < adContentIdArray.size(); i++) {
            int adContentId = Integer.parseInt(adContentIdArray.getString(i).trim());
            ConfigAdContent configAdContent = this.configAdContentService.getCacheEntity(ConfigAdContent.class, adContentId);

            JSONArray adContentArray = new JSONArray();
            switch (adContentId) {
                // 空白插屏
                case -4:
                    // 空白Banner
                case -2:
                    adContentArray.add(0);
                    break;
                // 平台插屏广告
                case -3:
                    if (configAdApp.isWxIntAllowedShow()) {
                        adContentArray.add(2);
                        // default开头的app配置表示当前使用的默认配置，则需要从wx_config表使用官方广告id
                        if (StringUtils.startsWith(configAdApp.getAppId(), "default")) {
                            adContentArray.add(wxConfig.getInit());
                        } else {
                            adContentArray.add(configAdApp.getWxIntUint());
                        }
                    }
                    break;
                // 平台Banner广告
                case -1:
                    if (configAdApp.isWxBannerAllowedShow()) {
                        adContentArray.add(2);
                        // default开头的app配置表示当前使用的默认配置，则需要从wx_config表使用官方广告id
                        if (StringUtils.startsWith(configAdApp.getAppId(), "default")) {
                            adContentArray.add(wxConfig.getBanner());
                        } else {
                            adContentArray.add(configAdApp.getWxBannerUnit());
                        }
                    }
                    break;
                default:
                    // 如果当前配置不允许展示自有广告或者是审核版本，不展示自有广告
                    if (configAdApp.isAllowedShow() && versionValue != checkVersionValue) {
                        // 1-自有广告
                        adContentArray.add(1);
                        adContentArray.add(configAdContent.getId());
                        adContentArray.add(configAdContent.getTargetAppId());
                        adContentArray.add(configAdContent.getTargetAppName());
                        adContentArray.add(configAdContent.getImageUrl().replace(URL, ""));

                        ConfigAdContentInfo configAdContentInfo = this.configAdContentInfoService.getCacheEntity(ConfigAdContentInfo.class, configAdContent.getTargetAppId());
                        JSONArray appInfo = new JSONArray();
                        appInfo.add(configAdContentInfo.getTargetAppType());
                        appInfo.add(configAdContentInfo.getTargetEnvVersion());
                        // 占位用
                        appInfo.add("");
                        appInfo.add(StringUtils.isNotBlank(configAdContentInfo.getTargetAppPage()) ? configAdContentInfo.getTargetAppPage() : "");
                        appInfo.add(StringUtils.isNotBlank(configAdContentInfo.getExtraParam()) ? JSONObject.parseObject(configAdContentInfo.getExtraParam()) : new JSONObject());
                        if (!StringUtils.equals(configAdContent.getTargetAppId(), configAdContentInfo.getPromoteAppId())) {
                            appInfo.add(configAdContentInfo.getPromoteAppId());
                            appInfo.add(configAdContentInfo.getPromoteEnvVersion());
                            appInfo.add(StringUtils.isNotBlank(configAdContentInfo.getPromoteAppPage()) ? configAdContentInfo.getPromoteAppPage() : "");
                        }
                        appInfoArray.put(configAdContent.getTargetAppId(), appInfo);
                    }
                    break;
            }
            if (!adContentArray.isEmpty()) {
                adContentPoolArray.add(adContentArray);
            }
        }
        adPool.put(adType, adContentPoolArray);
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setConfigAdAppService(ConfigAdAppService configAdAppService) {
        this.configAdAppService = configAdAppService;
    }

    @Autowired
    public void setConfigAdContentPoolService(ConfigAdContentPoolService configAdContentPoolService) {
        this.configAdContentPoolService = configAdContentPoolService;
    }

    @Autowired
    public void setConfigAdPositionService(ConfigAdPositionService configAdPositionService) {
        this.configAdPositionService = configAdPositionService;
    }

    @Autowired
    public void setConfigAdContentService(ConfigAdContentService configAdContentService) {
        this.configAdContentService = configAdContentService;
    }

    @Autowired
    public void setConfigAdContentInfoService(ConfigAdContentInfoService configAdContentInfoService) {
        this.configAdContentInfoService = configAdContentInfoService;
    }

    @Autowired
    public void setWxAppConfigService(WxAppConfigService wxAppConfigService) {
        this.wxAppConfigService = wxAppConfigService;
    }

}
