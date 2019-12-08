package com.fish.dao.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StatisticsRetention
{
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date newDate;

    private Long newUser;

    private Long retained1;

    private Long retained2;

    private Long retained3;

    private Long retained4;

    private Long retained5;

    private Long retained6;

    private Long retained7;

    private Long retained8;

    private Long retained9;

    private Long retained10;

    private Long retained11;

    private Long retained12;

    private Long retained13;

    private Long retained14;

    private Long retained15;

    private Long retained16;

    private Long retained17;

    public Date getNewDate()
    {
        return newDate;
    }

    public void setNewDate(Date newDate)
    {
        this.newDate = newDate;
    }

    public Long getNewUser()
    {
        return newUser;
    }

    public void setNewUser(Long newUser)
    {
        this.newUser = newUser;
    }

    public Long getRetained1()
    {
        return retained1;
    }

    public void setRetained1(Long retained1)
    {
        this.retained1 = retained1;
    }

    public Long getRetained2()
    {
        return retained2;
    }

    public void setRetained2(Long retained2)
    {
        this.retained2 = retained2;
    }

    public Long getRetained3()
    {
        return retained3;
    }

    public void setRetained3(Long retained3)
    {
        this.retained3 = retained3;
    }

    public Long getRetained4()
    {
        return retained4;
    }

    public void setRetained4(Long retained4)
    {
        this.retained4 = retained4;
    }

    public Long getRetained5()
    {
        return retained5;
    }

    public void setRetained5(Long retained5)
    {
        this.retained5 = retained5;
    }

    public Long getRetained6()
    {
        return retained6;
    }

    public void setRetained6(Long retained6)
    {
        this.retained6 = retained6;
    }

    public Long getRetained7()
    {
        return retained7;
    }

    public void setRetained7(Long retained7)
    {
        this.retained7 = retained7;
    }

    public Long getRetained8()
    {
        return retained8;
    }

    public void setRetained8(Long retained8)
    {
        this.retained8 = retained8;
    }

    public Long getRetained9()
    {
        return retained9;
    }

    public void setRetained9(Long retained9)
    {
        this.retained9 = retained9;
    }

    public Long getRetained10()
    {
        return retained10;
    }

    public void setRetained10(Long retained10)
    {
        this.retained10 = retained10;
    }

    public Long getRetained11()
    {
        return retained11;
    }

    public void setRetained11(Long retained11)
    {
        this.retained11 = retained11;
    }

    public Long getRetained12()
    {
        return retained12;
    }

    public void setRetained12(Long retained12)
    {
        this.retained12 = retained12;
    }

    public Long getRetained13()
    {
        return retained13;
    }

    public void setRetained13(Long retained13)
    {
        this.retained13 = retained13;
    }

    public Long getRetained14()
    {
        return retained14;
    }

    public void setRetained14(Long retained14)
    {
        this.retained14 = retained14;
    }

    public Long getRetained15()
    {
        return retained15;
    }

    public void setRetained15(Long retained15)
    {
        this.retained15 = retained15;
    }

    public Long getRetained16()
    {
        return retained16;
    }

    public void setRetained16(Long retained16)
    {
        this.retained16 = retained16;
    }

    public Long getRetained17()
    {
        return retained17;
    }

    public void setRetained17(Long retained17)
    {
        this.retained17 = retained17;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", newDate=").append(newDate);
        sb.append(", newUser=").append(newUser);
        sb.append(", retained1=").append(retained1);
        sb.append(", retained2=").append(retained2);
        sb.append(", retained3=").append(retained3);
        sb.append(", retained4=").append(retained4);
        sb.append(", retained5=").append(retained5);
        sb.append(", retained6=").append(retained6);
        sb.append(", retained7=").append(retained7);
        sb.append(", retained8=").append(retained8);
        sb.append(", retained9=").append(retained9);
        sb.append(", retained10=").append(retained10);
        sb.append(", retained11=").append(retained11);
        sb.append(", retained12=").append(retained12);
        sb.append(", retained13=").append(retained13);
        sb.append(", retained14=").append(retained14);
        sb.append(", retained15=").append(retained15);
        sb.append(", retained16=").append(retained16);
        sb.append(", retained17=").append(retained17);
        sb.append("]");
        return sb.toString();
    }
}