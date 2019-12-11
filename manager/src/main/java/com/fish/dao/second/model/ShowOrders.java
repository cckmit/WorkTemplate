package com.fish.dao.second.model;

import org.springframework.stereotype.Component;

@Component
public class ShowOrders {
    private Orders orders;
    private String productName;
    private String originName;
    private String goodsName;
    private String ddDesc;
    private String userName;

    public String getDdDesc() {
        return ddDesc;
    }

    public void setDdDesc(String ddDesc) {
        this.ddDesc = ddDesc;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}