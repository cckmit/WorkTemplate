package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 广告类型
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-25 17:47
 */
public class ConfigAdType {
    /**
     * 广告类型ID
     */
    private Integer ddId;
    /**
     * 广告类型名称
     */
    private String ddName;
    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

    public Integer getDdId() { return ddId; }

    public void setDdId(Integer ddId) { this.ddId = ddId; }

    public String getDdName() { return ddName; }

    public void setDdName(String ddName) { this.ddName = ddName; }

    public Date getUpdateTime() { return updateTime; }

    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

}
