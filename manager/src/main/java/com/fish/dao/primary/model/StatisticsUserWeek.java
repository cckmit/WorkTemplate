package com.fish.dao.primary.model;

public class StatisticsUserWeek
{
    private Integer newWeek;

    private Long wnu;

    private Long wau;

    private Long wosu;

    private Long payUser;

    private Long serviceTime;

    private Long pcu;

    private Long acu;

    public Integer getNewWeek()
    {
        return newWeek;
    }

    public void setNewWeek(Integer newWeek)
    {
        this.newWeek = newWeek;
    }

    public Long getWnu()
    {
        return wnu;
    }

    public void setWnu(Long wnu)
    {
        this.wnu = wnu;
    }

    public Long getWau()
    {
        return wau;
    }

    public void setWau(Long wau)
    {
        this.wau = wau;
    }

    public Long getWosu()
    {
        return wosu;
    }

    public void setWosu(Long wosu)
    {
        this.wosu = wosu;
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
        sb.append(", newWeek=").append(newWeek);
        sb.append(", wnu=").append(wnu);
        sb.append(", wau=").append(wau);
        sb.append(", wosu=").append(wosu);
        sb.append(", payUser=").append(payUser);
        sb.append(", serviceTime=").append(serviceTime);
        sb.append(", pcu=").append(pcu);
        sb.append(", acu=").append(acu);
        sb.append("]");
        return sb.toString();
    }
}