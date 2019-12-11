package com.fish.dao.second.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class WxConfig {
    private String ddappid;

    private String ddappsecret;

    private String ddmchid;

    private String ddp12password;

    private String ddkey;

    private String ddp12;

    private Integer programType;

    private String originId;

    private String productName;

    private String originName;

    private String addId;

    private String account;

    private String password;

    private String codeManager;

    private String managerWechat;
    private String belongCompany;

    private String clearCompany;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",  timezone="GMT+8")
    private Date createTime;

    private String jumpDirect;

    public String getManagerWechat() {
        return managerWechat;
    }

    public void setManagerWechat(String managerWechat) {
        this.managerWechat = managerWechat;
    }

    public String getJumpDirect() {
        return jumpDirect;
    }

    public void setJumpDirect(String jumpDirect) {
        this.jumpDirect = jumpDirect;
    }

    public String getDdappid() {
        return ddappid;
    }

    public void setDdappid(String ddappid) {
        this.ddappid = ddappid == null ? null : ddappid.trim();
    }

    public String getDdappsecret() {
        return ddappsecret;
    }

    public void setDdappsecret(String ddappsecret) {
        this.ddappsecret = ddappsecret == null ? null : ddappsecret.trim();
    }

    public String getDdmchid() {
        return ddmchid;
    }

    public void setDdmchid(String ddmchid) {
        this.ddmchid = ddmchid == null ? null : ddmchid.trim();
    }

    public String getDdp12password() {
        return ddp12password;
    }

    public void setDdp12password(String ddp12password) {
        this.ddp12password = ddp12password == null ? null : ddp12password.trim();
    }

    public String getDdkey() {
        return ddkey;
    }

    public void setDdkey(String ddkey) {
        this.ddkey = ddkey == null ? null : ddkey.trim();
    }

    public String getDdp12() {
        return ddp12;
    }

    public void setDdp12(String ddp12) {
        this.ddp12 = ddp12 == null ? null : ddp12.trim();
    }

    public Integer getProgramType() {
        return programType;
    }

    public void setProgramType(Integer programType) {
        this.programType = programType;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId == null ? null : originId.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName == null ? null : originName.trim();
    }

    public String getAddId() {
        return addId;
    }

    public void setAddId(String addId) {
        this.addId = addId == null ? null : addId.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getCodeManager() {
        return codeManager;
    }

    public void setCodeManager(String codeManager) {
        this.codeManager = codeManager == null ? null : codeManager.trim();
    }

    public String getBelongCompany() {
        return belongCompany;
    }

    public void setBelongCompany(String belongCompany) {
        this.belongCompany = belongCompany == null ? null : belongCompany.trim();
    }

    public String getClearCompany() {
        return clearCompany;
    }

    public void setClearCompany(String clearCompany) {
        this.clearCompany = clearCompany == null ? null : clearCompany.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ddappid=").append(ddappid);
        sb.append(", ddappsecret=").append(ddappsecret);
        sb.append(", ddmchid=").append(ddmchid);
        sb.append(", ddp12password=").append(ddp12password);
        sb.append(", ddkey=").append(ddkey);
        sb.append(", ddp12=").append(ddp12);
        sb.append(", programType=").append(programType);
        sb.append(", originId=").append(originId);
        sb.append(", productName=").append(productName);
        sb.append(", originName=").append(originName);
        sb.append(", addId=").append(addId);
        sb.append(", account=").append(account);
        sb.append(", password=").append(password);
        sb.append(", codeManager=").append(codeManager);
        sb.append(", belongCompany=").append(belongCompany);
        sb.append(", clearCompany=").append(clearCompany);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}