package com.blazefire.service;

import com.alibaba.fastjson.JSONObject;
import com.blazefire.dao.second.mapper.WxAdValueMapper;
import org.springframework.stereotype.Service;

/**
 * Opc数据查询
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-4-23 15:32
 */
@Service
public class OpcDataService {

    private WxAdValueMapper wxAdValueMapper;

    /**
     * @param appId    appId
     * @param dataDate 查询日期，要求yyyyMMdd格式
     * @return 数据结果Json对象
     */
    public JSONObject getData(String appId, String dataDate) {
        JSONObject resultObject = new JSONObject();
        // 1、总收入：视频1+其它插屏+其它banner

        // 2、总活跃：街机后台-用户来源明细-活跃-搜索来源

        // 3、视频数据：视频1数据

        // 4、Banner数据：其它Banner

        return resultObject;
    }

}
