package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class OnlineStatistics {
    private Integer id;

    private Integer ddcode;

    private Long ddonlinecount;

    private Long ddgameroomcount;

    private Long ddremainroomcount;

    private String ddgamedistribution;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDdcode() {
        return ddcode;
    }

    public void setDdcode(Integer ddcode) {
        this.ddcode = ddcode;
    }

    public Long getDdonlinecount() {
        return ddonlinecount;
    }

    public void setDdonlinecount(Long ddonlinecount) {
        this.ddonlinecount = ddonlinecount;
    }

    public Long getDdgameroomcount() {
        return ddgameroomcount;
    }

    public void setDdgameroomcount(Long ddgameroomcount) {
        this.ddgameroomcount = ddgameroomcount;
    }

    public Long getDdremainroomcount() {
        return ddremainroomcount;
    }

    public void setDdremainroomcount(Long ddremainroomcount) {
        this.ddremainroomcount = ddremainroomcount;
    }

    public String getDdgamedistribution() {
        return ddgamedistribution;
    }

    public void setDdgamedistribution(String ddgamedistribution) {
        this.ddgamedistribution = ddgamedistribution == null ? null : ddgamedistribution.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreateTime() {
        return createTime;
    }


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddcode=").append(ddcode);
        sb.append(", ddonlinecount=").append(ddonlinecount);
        sb.append(", ddgameroomcount=").append(ddgameroomcount);
        sb.append(", ddremainroomcount=").append(ddremainroomcount);
        sb.append(", ddgamedistribution=").append(ddgamedistribution);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}