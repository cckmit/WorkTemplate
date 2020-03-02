package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 微信群管理
 */
@Component
public class WxGroupHistory {
    /**
     * wx_group_manager 部分内容
     */
    private Integer id;

    /**
     * 微信群名
     */
    private String wxGroupName;
    /**
     * 微信群管理
     */
    private String wxGroupManager;


    private String createTime;

    /**
     * *微信群备注
     */
    private String describe;
    /**
     *  二维码状态 0：关闭；1开启
     */
    private Integer ddStatus;
    /**
     *  微信群二维码地址
     */
    private String ddYes;
    /**
     * 客服二维码
     */
    private String ddNo;
    /**
     * 更新表的时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新二维码时间
     */
    private String updateQrCodeTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWxGroupName() {
        return wxGroupName;
    }

    public void setWxGroupName(String wxGroupName) {
        this.wxGroupName = wxGroupName;
    }

    public String getWxGroupManager() {
        return wxGroupManager;
    }

    public void setWxGroupManager(String wxGroupManager) {
        this.wxGroupManager = wxGroupManager;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getDdStatus() {
        return ddStatus;
    }

    public void setDdStatus(Integer ddStatus) {
        this.ddStatus = ddStatus;
    }

    public String getDdYes() {
        return ddYes;
    }

    public void setDdYes(String ddYes) {
        this.ddYes = ddYes;
    }

    public String getDdNo() {
        return ddNo;
    }

    public void setDdNo(String ddNo) {
        this.ddNo = ddNo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateQrCodeTime() {
        return updateQrCodeTime;
    }

    public void setUpdateQrCodeTime(String updateQrCodeTime) {
        this.updateQrCodeTime = updateQrCodeTime;
    }
}
