package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 广告组合
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-13 16:59
 */
public class ConfigAdCombination {
    /**
     * 自增ID
     */
    private int ddId;
    /**
     * 名称
     */
    private String ddName;
    /**
     * 配置Json
     */
    private String ddJson;
    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    public int getDdId() {
        return ddId;
    }

    public void setDdId(int ddId) {
        this.ddId = ddId;
    }

    public String getDdName() {
        return ddName;
    }

    public void setDdName(String ddName) {
        this.ddName = ddName;
    }

    public String getDdJson() {
        return ddJson;
    }

    public void setDdJson(String ddJson) {
        this.ddJson = ddJson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
