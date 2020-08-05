package com.blazefire.service.entity;

/**
 * 小游戏/小程序信息
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-27 15:45
 */
public class WxAppAccessToken {

    private String appId;

    private String appSecret;

    private String accessToken;

    private String source;

    private int type;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
