package com.fish.dao.second.model;

public class NoticeWithBLOBs extends Notice
{
    private String msg;

    private String reward;

    private String userlist;

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg == null ? null : msg.trim();
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward == null ? null : reward.trim();
    }

    public String getUserlist()
    {
        return userlist;
    }

    public void setUserlist(String userlist)
    {
        this.userlist = userlist == null ? null : userlist.trim();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", msg=").append(msg);
        sb.append(", reward=").append(reward);
        sb.append(", userlist=").append(userlist);
        sb.append("]");
        return sb.toString();
    }
}