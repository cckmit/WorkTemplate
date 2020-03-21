package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 广告内容
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdContent {

    private int ddId;
    private int ddAdType;
    private int ddWeight;
    private String ddImageUrl;
    private String ddTargetAppId;
    private String ddTargetAppName;
    private int ddTargetAppType;
    private String ddTargetEnvVersion;
    private String ddTargetAppPage;
    private String ddPromoteAppId;
    private String ddPromoteAppName;
    private int ddPromoteAppType;
    private String ddPromoteEnvVersion;
    private String ddPromoteAppPage;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    /**
     * 展示数据-由ddAdType关联得到的广告内容名称
     */
    private String adTypeName;

    public int getDdId() { return ddId; }

    public void setDdId(int ddId) { this.ddId = ddId; }

    public int getDdAdType() { return ddAdType; }

    public void setDdAdType(int ddAdType) { this.ddAdType = ddAdType; }

    public int getDdWeight() { return ddWeight; }

    public void setDdWeight(int ddWeight) { this.ddWeight = ddWeight; }

    public String getDdImageUrl() { return ddImageUrl; }

    public void setDdImageUrl(String ddImageUrl) { this.ddImageUrl = ddImageUrl; }

    public String getDdTargetAppId() { return ddTargetAppId; }

    public void setDdTargetAppId(String ddTargetAppId) { this.ddTargetAppId = ddTargetAppId; }

    public String getDdTargetAppName() { return ddTargetAppName; }

    public void setDdTargetAppName(String ddTargetAppName) { this.ddTargetAppName = ddTargetAppName;}

    public int getDdTargetAppType() { return ddTargetAppType; }

    public void setDdTargetAppType(int ddTargetAppType) { this.ddTargetAppType = ddTargetAppType; }

    public String getDdTargetEnvVersion() { return ddTargetEnvVersion; }

    public void setDdTargetEnvVersion(String ddTargetEnvVersion) { this.ddTargetEnvVersion = ddTargetEnvVersion; }

    public String getDdTargetAppPage() { return ddTargetAppPage; }

    public void setDdTargetAppPage(String ddTargetAppPage) { this.ddTargetAppPage = ddTargetAppPage; }

    public String getDdPromoteAppId() { return ddPromoteAppId; }

    public void setDdPromoteAppId(String ddPromoteAppId) { this.ddPromoteAppId = ddPromoteAppId; }

    public String getDdPromoteAppName() { return ddPromoteAppName; }

    public void setDdPromoteAppName(String ddPromoteAppName) { this.ddPromoteAppName = ddPromoteAppName; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getAdTypeName() { return adTypeName; }

    public void setAdTypeName(String adTypeName) { this.adTypeName = adTypeName; }

    public int getDdPromoteAppType() { return ddPromoteAppType; }

    public void setDdPromoteAppType(int ddPromoteAppType) { this.ddPromoteAppType = ddPromoteAppType; }

    public String getDdPromoteEnvVersion() { return ddPromoteEnvVersion; }

    public void setDdPromoteEnvVersion(String ddPromoteEnvVersion) { this.ddPromoteEnvVersion = ddPromoteEnvVersion; }

    public String getDdPromoteAppPage() { return ddPromoteAppPage; }

    public void setDdPromoteAppPage(String ddPromoteAppPage) { this.ddPromoteAppPage = ddPromoteAppPage; }

}
