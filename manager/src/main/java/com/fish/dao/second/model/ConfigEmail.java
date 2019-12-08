package com.fish.dao.second.model;

public class ConfigEmail
{
    private String udptype;

    private String title;

    private String msg;

    private String button;

    private String reward;

    public String getUdptype()
    {
        return udptype;
    }

    public void setUdptype(String udptype)
    {
        this.udptype = udptype == null ? null : udptype.trim();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title == null ? null : title.trim();
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg == null ? null : msg.trim();
    }

    public String getButton()
    {
        return button;
    }

    public void setButton(String button)
    {
        this.button = button == null ? null : button.trim();
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward == null ? null : reward.trim();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", udptype=").append(udptype);
        sb.append(", title=").append(title);
        sb.append(", msg=").append(msg);
        sb.append(", button=").append(button);
        sb.append(", reward=").append(reward);
        sb.append("]");
        return sb.toString();
    }
}