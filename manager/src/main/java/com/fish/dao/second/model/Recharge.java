package com.fish.dao.second.model;

import java.math.BigDecimal;
import java.util.Date;

public class Recharge {
    private  String ddopenid;

    private String ddid;

    private String dduid;

    private String ddappid;

    private BigDecimal ddrmb;

    private String ddtip;

    private Integer ddstatus;

    private Date ddtrans;

    private Date ddtimes;

    public String getDdopenid() {
        return ddopenid;
    }

    public void setDdopenid(String ddopenid) {
        this.ddopenid = ddopenid;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid == null ? null : ddid.trim();
    }

    public String getDduid() {
        return dduid;
    }

    public void setDduid(String dduid) {
        this.dduid = dduid == null ? null : dduid.trim();
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public BigDecimal getDdrmb() {
        return ddrmb;
    }

    public void setDdrmb(BigDecimal ddrmb) {
        this.ddrmb = ddrmb;
    }

    public String getDdtip() {
        return ddtip;
    }

    public void setDdtip(String ddtip) {
        this.ddtip = ddtip == null ? null : ddtip.trim();
    }

    public Integer getDdstatus() {
        return ddstatus;
    }

    public void setDdstatus(Integer ddstatus) {
        this.ddstatus = ddstatus;
    }

    public Date getDdtrans() {
        return ddtrans;
    }

    public void setDdtrans(Date ddtrans) {
        this.ddtrans = ddtrans;
    }

    public Date getDdtimes() {
        return ddtimes;
    }

    public void setDdtimes(Date ddtimes) {
        this.ddtimes = ddtimes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddid=").append(ddid);
        sb.append(", dduid=").append(dduid);
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddrmb=").append(ddrmb);
        sb.append(", ddtip=").append(ddtip);
        sb.append(", ddstatus=").append(ddstatus);
        sb.append(", ddtrans=").append(ddtrans);
        sb.append(", ddtimes=").append(ddtimes);
        sb.append("]");
        return sb.toString();
    }
}