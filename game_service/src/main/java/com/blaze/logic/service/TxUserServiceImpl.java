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
 * 腾讯用户信息处理逻辑Service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-10 16:36
 */
@Service
public class TxUserServiceImpl extends BaseUserService {

    @Override
    protected String getUid(JSONObject loginInfo) {
        String uid = null;
        String appId = loginInfo.getString(RequestConst.APP_ID);
        String code = loginInfo.getString(RequestConst.CODE);
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, appId);
        if (wxConfig != null) {
            // 游客登录
            String loginUrl = MessageFormat.format(this.businessConfig.getWxLoginUrl(), wxConfig.getAppId(),
                    wxConfig.getAppSecret(), code);
            String loginResult = HttpUtil.get(loginUrl);
            if (StringUtils.isNotBlank(loginResult)) {
                JSONObject loginObject = JSON.parseObject(loginResult);
                if (loginObject != null && loginObject.containsKey("openid")) {
                    uid = loginObject.getString("openid");
                } else {
                    LOGGER.error(appId + "|" + code + " -> 游客登录请求返回失败！");
                }
            } else {
                LOGGER.error(appId + "|" + code + " -> 游客登录请求异常！");
            }
        } else {
            LOGGER.error(appId + " -> AppId错误！");
        }
        return uid;
    }

    @Override
    protected String getOpenId(JSONObject bindInfo) {
        return getUid(bindInfo);
    }

}
