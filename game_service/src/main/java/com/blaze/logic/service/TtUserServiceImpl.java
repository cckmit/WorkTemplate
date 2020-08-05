package com.blaze.logic.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blaze.data.entity.WxConfig;
import com.blaze.logic.RequestConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * 头条用户信息处理逻辑Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-10 11:28
 */
@Service
public class TtUserServiceImpl extends BaseUserService {

    /**
     * 通过设备ID注册用户
     *
     * @param loginInfo 登录信息
     * @return 用户信息
     */
    @Override
    protected String getUid(JSONObject loginInfo) {
        String uid = null;
        String appId = loginInfo.getString(RequestConst.APP_ID);
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, appId);
        if (wxConfig != null) {
            // 游客登录
            String anonymousCode = loginInfo.getString("anonymousCode");
            String loginUrl = MessageFormat.format(this.businessConfig.getTtLoginUrl(), wxConfig.getAppId(),
                    wxConfig.getAppSecret(), "", anonymousCode);
            String loginResult = HttpUtil.get(loginUrl);
            if (StringUtils.isNotBlank(loginResult)) {
                JSONObject loginObject = JSON.parseObject(loginResult);
                if (loginObject != null && StringUtils.equals(loginObject.getString("error"), "0")) {
                    uid = loginObject.getString("anonymous_openid");
                } else {
                    LOGGER.error(appId + "|" + anonymousCode + " -> 匿名登录请求返回失败！");
                }
            } else {
                LOGGER.error(appId + "|" + anonymousCode + " -> 匿名登录请求异常！");
            }
        } else {
            LOGGER.error(appId + " -> AppId错误！");
        }
        return uid;
    }

    @Override
    protected String getOpenId(JSONObject bindInfo) {
        String openId = null;
        String appId = bindInfo.getString(RequestConst.APP_ID);
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, appId);
        String code = bindInfo.getString(RequestConst.CODE);
        if (StringUtils.isNotBlank(code)) {
            String loginUrl = MessageFormat.format(this.businessConfig.getTtLoginUrl(), wxConfig.getAppId(),
                    wxConfig.getAppSecret(), code, "");
            String loginResult = HttpUtil.get(loginUrl);
            LOGGER.debug(loginResult);
            if (StringUtils.isNotBlank(loginResult)) {
                JSONObject loginObject = JSON.parseObject(loginResult);
                if (loginObject != null && StringUtils.equals(loginObject.getString("error"), "0")) {
                    openId = loginObject.getString("openid");
                } else {
                    LOGGER.error(appId + "|" + code + " -> 登录请求返回失败！");
                }
            } else {
                LOGGER.error(appId + "|" + code + " -> 登录请求异常！");
            }
        }
        return openId;
    }

}
