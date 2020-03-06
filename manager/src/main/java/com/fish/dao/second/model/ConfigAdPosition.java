package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 广告位置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdPosition {
    // 以下是数据库字段属性************************************************/

    private int ddId;
    private String ddName;
    private String ddAdTypes;
    private boolean ddAllowedOperation;
    private String ddAdSpaces;
    private int ddStrategyId;
    private String ddStrategyValue;
    private boolean ddShowWxAd;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    /**
     * 展示数据-由ddAdTypes关联的广告类型名称
     */
    private String adTypeNames;
    /**
     * 展示数据-由ddAdSpaces关联的广告位名称
     */
    private String adSpaceNames;
    /**
     * 展示数据-由ddId关联的广告策略名称
     */
    private String adStrategyNames;


    public int getDdId() { return ddId; }

    public void setDdId(int ddId) { this.ddId = ddId; }

    public String getDdName() { return ddName; }

    public void setDdName(String ddName) { this.ddName = ddName; }

    public String getDdAdTypes() { return ddAdTypes; }

    public void setDdAdTypes(String ddAdTypes) { this.ddAdTypes = ddAdTypes; }

    public boolean isDdAllowedOperation() { return ddAllowedOperation; }

    public void setDdAllowedOperation(boolean ddAllowedOperation) { this.ddAllowedOperation = ddAllowedOperation;}

    public String getDdAdSpaces() { return ddAdSpaces; }

    public void setDdAdSpaces(String ddAdSpaces) { this.ddAdSpaces = ddAdSpaces; }

    public int getDdStrategyId() { return ddStrategyId; }

    public void setDdStrategyId(int ddStrategyId) { this.ddStrategyId = ddStrategyId; }

    public String getDdStrategyValue() { return ddStrategyValue; }

    public void setDdStrategyValue(String ddStrategyValue) { this.ddStrategyValue = ddStrategyValue; }

    public boolean isDdShowWxAd() { return ddShowWxAd; }

    public void setDdShowWxAd(boolean ddShowWxAd) { this.ddShowWxAd = ddShowWxAd; }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAdTypeNames() { return adTypeNames; }

    public void setAdTypeNames(String adTypeNames) { this.adTypeNames = adTypeNames; }

    public String getAdSpaceNames() { return adSpaceNames; }

    public void setAdSpaceNames(String adSpaceNames) { this.adSpaceNames = adSpaceNames; }

    public String getAdStrategyNames() {
        return adStrategyNames;
    }

    public void setAdStrategyNames(String adStrategyNames) {
        this.adStrategyNames = adStrategyNames;
    }
}
