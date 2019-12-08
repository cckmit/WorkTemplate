package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StatisticsUserDay
{
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date newDate;

    private Long dnu;

    private Long dau;

    private Long dosu;

    private Long payUser;

    private Long serviceTime;

    private Long pcu;

    private Long acu;

    private Long mau;

    public Long getMau()
    {
        return mau;
    }

    public void setMau(Long mau)
    {
        this.mau = mau;
    }

    public Date getNewDate()
    {
        return newDate;
    }

    public void setNewDate(Date newDate)
    {
        this.newDate = newDate;
    }

    public Long getDnu()
    {
        return dnu;
    }

    public void setDnu(Long dnu)
    {
        this.dnu = dnu;
    }

    public Long getDau()
    {
        return dau;
    }

    public void setDau(Long dau)
    {
        this.dau = dau;
    }

    public Long getDosu()
    {
        return dosu;
    }

    public void setDosu(Long dosu)
    {
        this.dosu = dosu;
    }

    public Long getPayUser()
    {
        return payUser;
    }

    public void setPayUser(Long payUser)
    {
        this.payUser = payUser;
    }

    public Long getServiceTime()
    {
        return serviceTime;
    }

    public void setServiceTime(Long serviceTime)
    {
        this.serviceTime = serviceTime;
    }

    public Long getPcu()
    {
        return pcu;
    }

    public void setPcu(Long pcu)
    {
        this.pcu = pcu;
    }

    public Long getAcu()
    {
        return acu;
    }

    public void setAcu(Long acu)
    {
        this.acu = acu;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", newDate=").append(newDate);
        sb.append(", dnu=").append(dnu);
        sb.append(", dau=").append(dau);
        sb.append(", dosu=").append(dosu);
        sb.append(", payUser=").append(payUser);
        sb.append(", serviceTime=").append(serviceTime);
        sb.append(", pcu=").append(pcu);
        sb.append(", acu=").append(acu);
        sb.append("]");
        return sb.toString();
    }
}