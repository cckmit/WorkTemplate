package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RecordBook
{
    private Long id;

    private Long userid;

    private String nickname;

    private Integer book;

    private String bookname;

    private Integer basinid;

    private String basinname;

    private Integer lighten;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date updatetime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserid()
    {
        return userid;
    }

    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public Integer getBook()
    {
        return book;
    }

    public void setBook(Integer book)
    {
        this.book = book;
    }

    public String getBookname()
    {
        return bookname;
    }

    public void setBookname(String bookname)
    {
        this.bookname = bookname == null ? null : bookname.trim();
    }

    public Integer getBasinid()
    {
        return basinid;
    }

    public void setBasinid(Integer basinid)
    {
        this.basinid = basinid;
    }

    public String getBasinname()
    {
        return basinname;
    }

    public void setBasinname(String basinname)
    {
        this.basinname = basinname == null ? null : basinname.trim();
    }

    public Integer getLighten()
    {
        return lighten;
    }

    public void setLighten(Integer lighten)
    {
        this.lighten = lighten;
    }

    public Date getUpdatetime()
    {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime)
    {
        this.updatetime = updatetime;
    }


    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }
}