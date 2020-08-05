package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 访问页面
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 18:05
 */
@Data
public class WxVisitPage {

    private String appId;
    private String refDate;
    private String pagePath;
    private int pageVisitPv;
    private int pageVisitUv;
    private double pageStaytimePv;
    private int entrypagePv;
    private int exitpagePv;
    private int pageSharePv;
    private int pageShareUv;

}
