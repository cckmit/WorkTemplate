package com.fish.protocols;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class StatisticsPayVO
{
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date payDate;

    private Integer newWeek;
    private Integer newMonth;

    private BigDecimal payIncome;

    private BigDecimal videoIncome;

    private Long payUser;

    private Long actives;

    private Long newUser;

    private Long osu;

    private BigDecimal promotionCosts;

    public Date getPayDate()
    {
        return payDate;
    }

    public Integer getNewWeek()
    {
        return newWeek;
    }

    public void setNewWeek(Integer newWeek)
    {
        this.newWeek = newWeek;
    }

    public Integer getNewMonth()
    {
        return newMonth;
    }

    public void setNewMonth(Integer newMonth)
    {
        this.newMonth = newMonth;
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

    public BigDecimal getVideoIncome()
    {
        return videoIncome;
    }

    public void setVideoIncome(BigDecimal videoIncome)
    {
        this.videoIncome = videoIncome;
    }

    public Long getPayUser()
    {
        return payUser;
    }

    public void setPayUser(Long payUser)
    {
        this.payUser = payUser;
    }

    public Long getActives()
    {
        return actives;
    }

    public void setActives(Long actives)
    {
        this.actives = actives;
    }

    public Long getNewUser()
    {
        return newUser;
    }

    public void setNewUser(Long newUser)
    {
        this.newUser = newUser;
    }

    public Long getOsu()
    {
        return osu;
    }

    public void setOsu(Long osu)
    {
        this.osu = osu;
    }

    public BigDecimal getPromotionCosts()
    {
        return promotionCosts;
    }

    public void setPromotionCosts(BigDecimal promotionCosts)
    {
        this.promotionCosts = promotionCosts;
    }

    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer("StatisticsPayVO{");
        sb.append("payDate=").append(payDate);
        sb.append(", newWeek=").append(newWeek);
        sb.append(", newMonth=").append(newMonth);
        sb.append(", payIncome=").append(payIncome);
        sb.append(", videoIncome=").append(videoIncome);
        sb.append(", payUser=").append(payUser);
        sb.append(", actives=").append(actives);
        sb.append(", newUser=").append(newUser);
        sb.append(", osu=").append(osu);
        sb.append(", promotionCosts=").append(promotionCosts);
        sb.append('}');
        return sb.toString();
    }
}
