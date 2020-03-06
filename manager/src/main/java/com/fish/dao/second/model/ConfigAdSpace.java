package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 广告位
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdSpace {

    private int ddId;
    private String ddName;
    private int ddAdType;
    private boolean ddAllowedOperation;
    private int ddWeight;
    private int ddStrategyId;
    private String ddStrategyValue;
    private String ddAdContents;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    /**
     * 展示数据-广告类型名称
     */
    private String adTypeName;
    /**
     * 展示数据-广告内容名称
     */
    private String adContentNames;

    /**
     * 展示数据-由ddId关联的广告策略名称
     */
    private String adStrategyNames;
    public int getDdId() { return ddId; }

    public void setDdId(int ddId) { this.ddId = ddId; }

    public String getDdName() { return ddName; }

    public void setDdName(String ddName) { this.ddName = ddName; }

    public int getDdAdType() { return ddAdType; }

    public void setDdAdType(int ddAdType) { this.ddAdType = ddAdType; }

    public boolean isDdAllowedOperation() { return ddAllowedOperation; }

    public void setDdAllowedOperation(boolean ddAllowedOperation) { this.ddAllowedOperation = ddAllowedOperation; }

    public int getDdWeight() { return ddWeight; }

    public void setDdWeight(int ddWeight) { this.ddWeight = ddWeight; }

    public int getDdStrategyId() { return ddStrategyId; }

    public void setDdStrategyId(int ddStrategyId) {
        this.ddStrategyId = ddStrategyId;
    }

    public String getDdStrategyValue() {
        return ddStrategyValue;
    }

    public void setDdStrategyValue(String ddStrategyValue) {
        this.ddStrategyValue = ddStrategyValue;
    }

    public String getDdAdContents() {
        return ddAdContents;
    }

    public void setDdAdContents(String ddAdContents) {
        this.ddAdContents = ddAdContents;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAdTypeName() { return adTypeName; }

    public void setAdTypeName(String adTypeName) { this.adTypeName = adTypeName; }

    public String getAdContentNames() { return adContentNames; }

    public void setAdContentNames(String adContentNames) { this.adContentNames = adContentNames; }

    public String getAdStrategyNames() {
        return adStrategyNames;
    }

    public void setAdStrategyNames(String adStrategyNames) {
        this.adStrategyNames = adStrategyNames;
    }
}
