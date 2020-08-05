package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 小程序新增或活跃用户的画像分布数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-08 00:23
 */
@Data
public class WxUserPortrait {

    private String appId;
    private String refDate;
    private int dateType;
    private String dataType;
    private String portraitType;
    private String portraitValue;

    public WxUserPortrait(String appId, String refDate, int dateType, String dataType) {
        this.appId = appId;
        this.refDate = refDate;
        this.dateType = dateType;
        this.dataType = dataType;
    }

}
