package com.cc.manager.common.result;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 统计模型请求参数
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-08 11:06
 */
@Data
public class StatsListParam implements Serializable {

    private String queryData;

    private JSONObject queryObject;
    /**
     * 当前页
     */
    private int page;
    /**
     * 每页显示条数
     */
    private int limit;
}
