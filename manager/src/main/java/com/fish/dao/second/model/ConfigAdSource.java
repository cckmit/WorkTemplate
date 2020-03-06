package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 广告源
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdSource {

    private int ddId;
    private String ddName;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    public int getDdId() { return ddId; }

    public void setDdId(int ddId) { this.ddId = ddId; }

    public String getDdName() { return ddName; }

    public void setDdName(String ddName) { this.ddName = ddName; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

}
