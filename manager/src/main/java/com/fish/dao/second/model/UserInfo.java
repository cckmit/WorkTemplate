package com.fish.dao.second.model;

import java.util.Date;

public class UserInfo
{
    private Long userid;

    private String nickname;

    private String icon;

    private String platform;

    private String device;

    private Date registtime;

    private Date logintime;

    public Long getUserid()
    {
        return userid;
    }

    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform == null ? null : platform.trim();
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device == null ? null : device.trim();
    }

    public Date getRegisttime()
    {
        return registtime;
    }

    public void setRegisttime(Date registtime)
    {
        this.registtime = registtime;
    }

    public Date getLogintime()
    {
        return logintime;
    }

    public void setLogintime(Date logintime)
    {
        this.logintime = logintime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userid=").append(userid);
        sb.append(", nickname=").append(nickname);
        sb.append(", icon=").append(icon);
        sb.append(", platform=").append(platform);
        sb.append(", device=").append(device);
        sb.append(", registtime=").append(registtime);
        sb.append(", logintime=").append(logintime);
        sb.append("]");
        return sb.toString();
    }
}