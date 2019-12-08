package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class StatisticsPay
{
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date payDate;

    private BigDecimal payIncome;

    private Long payCount;

    public Date getPayDate()
    {
        return payDate;
    }

    public void setPayDate(Date payDate)
    {
        this.payDate = payDate;
    }

    public BigDecimal getPayIncome()
    {
        return payIncome;
    }

    public void setPayIncome(BigDecimal payIncome)
    {
        this.payIncome = payIncome;
    }

    public Long getPayCount()
    {
        return payCount;
    }

    public void setPayCount(Long payCount)
    {
        this.payCount = payCount;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", payDate=").append(payDate);
        sb.append(", payIncome=").append(payIncome);
        sb.append(", payCount=").append(payCount);
        sb.append("]");
        return sb.toString();
    }
}