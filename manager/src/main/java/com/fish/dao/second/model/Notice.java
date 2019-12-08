package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Notice
{
    private Integer id;

    private Boolean state;

    private String icon;

    private String title;

    private String button;

    private String leavetype;

    private Integer include;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date starttime;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date endtime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Boolean getState()
    {
        return state;
    }

    public void setState(Boolean state)
    {
        this.state = state;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title == null ? null : title.trim();
    }

    public String getButton()
    {
        return button;
    }

    public void setButton(String button)
    {
        this.button = button == null ? null : button.trim();
    }

    public String getLeavetype()
    {
        return leavetype;
    }

    public void setLeavetype(String leavetype)
    {
        this.leavetype = leavetype == null ? null : leavetype.trim();
    }

    public Integer getInclude()
    {
        return include;
    }

    public void setInclude(Integer include)
    {
        this.include = include;
    }

    public Date getStarttime()
    {
        return starttime;
    }

    public void setStarttime(Date starttime)
    {
        this.starttime = starttime;
    }

    public Date getEndtime()
    {
        return endtime;
    }

    public void setEndtime(Date endtime)
    {
        this.endtime = endtime;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", state=").append(state);
        sb.append(", icon=").append(icon);
        sb.append(", title=").append(title);
        sb.append(", button=").append(button);
        sb.append(", leavetype=").append(leavetype);
        sb.append(", include=").append(include);
        sb.append(", starttime=").append(starttime);
        sb.append(", endtime=").append(endtime);
        sb.append("]");
        return sb.toString();
    }
}