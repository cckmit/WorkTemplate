package com.fish.dao.second.model;

import java.util.Date;

/**
 * 微信广告配置
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdWx {

    private int ddId;
    private String ddName;
    private String ddAdUnit;
    private boolean ddAllowedShow;
    private int ddTime;
    private int ddStrategyId;
    private String ddStrategyValue;
    private Date updateTime;

    public int getDdId() { return ddId; }

    public void setDdId(int ddId) { this.ddId = ddId; }

    public String getDdName() { return ddName; }

    public void setDdName(String ddName) { this.ddName = ddName; }

    public String getDdAdUnit() { return ddAdUnit; }

    public void setDdAdUnit(String ddAdUnit) { this.ddAdUnit = ddAdUnit; }

    public boolean isDdAllowedShow() { return ddAllowedShow; }

    public void setDdAllowedShow(boolean ddAllowedShow) { this.ddAllowedShow = ddAllowedShow; }

    public int getDdTime() { return ddTime; }

    public void setDdTime(int ddTime) { this.ddTime = ddTime; }

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
