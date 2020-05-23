package com.cc.manager.common.result;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * get获取数据结果
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-22 20:42
 */
@Data
public class GetResult {

    /**
     * 执行结果代码，1-成功，2-失败
     */
    private int code = 1;

    /**
     * 执行结果信息
     */
    private String msg = "操作成功！";

    /**
     * 查询返回值
     */
    private JSONObject dataJson;

    /**
     * 查询返回数组
     */
    private JSONArray dataJsonArray;

    /**
     * 折线图，需要的时候
     */
    private JSONObject dataLineJson;

}
