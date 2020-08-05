package com.blazefire.dao.second.model;

import lombok.Data;

/**
 * 用户小程序访问分布数据
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-01 17:54
 */
@Data
public class WxVisitDistribution {

    private String appId;
    private String refDate;
    private String accessType;
    private String accessValue;

}
