package com.blazefire.service.entity;

/**
 * 小游戏/小程序信息
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-27 15:45
 */
public class AppInfo {

    private String appId;

    private String name;

    private String source;

    private String platform;

    private int type;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
