package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RecordVirtual
{
    private String id;

    private Long userid;

    private String nickname;

    private Integer goodsid;

    private Boolean firstsave;

    private Integer balance;

    private Integer historybalance;

    private Integer savesum;

    private Integer virtualvalue;

    private Boolean success;

    private String wealthtype;

    private Integer price;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date inserttime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id == null ? null : id.trim();
    }

    public Long getUserid()
    {
        return userid;
    }

    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public Integer getGoodsid()
    {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid)
    {
        this.goodsid = goodsid;
    }

    public Boolean getFirstsave()
    {
        return firstsave;
    }

    public void setFirstsave(Boolean firstsave)
    {
        this.firstsave = firstsave;
    }

    public Integer getBalance()
    {
        return balance;
    }

    public void setBalance(Integer balance)
    {
        this.balance = balance;
    }

    public Integer getHistorybalance()
    {
        return historybalance;
    }

    public void setHistorybalance(Integer historybalance)
    {
        this.historybalance = historybalance;
    }

    public Integer getSavesum()
    {
        return savesum;
    }

    public void setSavesum(Integer savesum)
    {
        this.savesum = savesum;
    }

    public Integer getVirtualvalue()
    {
        return virtualvalue;
    }

    public void setVirtualvalue(Integer virtualvalue)
    {
        this.virtualvalue = virtualvalue;
    }

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public String getWealthtype()
    {
        return wealthtype;
    }

    public void setWealthtype(String wealthtype)
    {
        this.wealthtype = wealthtype == null ? null : wealthtype.trim();
    }

    public Integer getPrice()
    {
        return price;
    }

    public void setPrice(Integer price)
    {
        this.price = price;
    }

    public Date getInserttime()
    {
        return inserttime;
    }

    public void setInserttime(Date inserttime)
    {
        this.inserttime = inserttime;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", goodsid=").append(goodsid);
        sb.append(", firstsave=").append(firstsave);
        sb.append(", balance=").append(balance);
        sb.append(", historybalance=").append(historybalance);
        sb.append(", savesum=").append(savesum);
        sb.append(", virtualvalue=").append(virtualvalue);
        sb.append(", success=").append(success);
        sb.append(", wealthtype=").append(wealthtype);
        sb.append(", price=").append(price);
        sb.append(", inserttime=").append(inserttime);
        sb.append("]");
        return sb.toString();
    }
}