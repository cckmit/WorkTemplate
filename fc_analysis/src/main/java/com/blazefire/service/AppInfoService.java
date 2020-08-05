package com.blazefire.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blazefire.config.WxConfig;
import com.blazefire.service.entity.AppInfo;
import com.blazefire.service.entity.WxAppAccessToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 从miniServer服务器获取微信app列表
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-22 10:53
 */
@Service
public class AppInfoService {

    private WxConfig wxConfig;

    /**
     * 获取全部app信息
     *
     * @return app信息列表
     */
    public List<AppInfo> getWxAppInfoList() {
        String accessTokenStr = HttpUtil.post(this.wxConfig.getAppInfoUrl() + "getAppInfo", "{\"appId\":\"ALL\"}");
        if (StringUtils.isNotBlank(accessTokenStr)) {
            JSONObject appInfoObject = JSONObject.parseObject(accessTokenStr);
            if (Objects.nonNull(appInfoObject) && StringUtils.equalsIgnoreCase("success",
                    appInfoObject.getString("result"))) {
                return JSONArray.parseArray(appInfoObject.getString("data"), AppInfo.class);
            }
        }
        return new ArrayList<>(0);
    }

    /**
     * 获取微信AccessToken信息
     *
     * @return
     */
    public List<WxAppAccessToken> getWxAppAccessTokenList() {
        String accessTokenStr = HttpUtil.post(this.wxConfig.getAppInfoUrl() + "getAppAccessToken", "{\"appId\":\"ALL\"}");
        if (StringUtils.isNotBlank(accessTokenStr)) {
            JSONObject accessTokenObject = JSONObject.parseObject(accessTokenStr);
            if (Objects.nonNull(accessTokenObject) && StringUtils.equalsIgnoreCase("success",
                    accessTokenObject.getString("result"))) {
                return JSONArray.parseArray(accessTokenObject.getString("data"), WxAppAccessToken.class);
            }
        }
        return new ArrayList<>(0);
    }


    @Autowired
    public void setWxConfig(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

}
