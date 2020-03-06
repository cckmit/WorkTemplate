package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 广告策略
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdStrategy {

    private int ddId;
    private String ddName;
    private String ddValueExample;
    private String ddDescription;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    public int getDdId() { return ddId; }

    public void setDdId(int ddId) {
        this.ddId = ddId;
    }

    public String getDdName() {
        return ddName;
    }

    public void setDdName(String ddName) {
        this.ddName = ddName;
    }

    public String getDdValueExample() {
        return ddValueExample;
    }

    public void setDdValueExample(String ddValueExample) {
        this.ddValueExample = ddValueExample;
    }

    public String getDdDescription() {
        return ddDescription;
    }

    public void setDdDescription(String ddDescription) {
        this.ddDescription = ddDescription;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
