package com.cc.manager.common.result;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计模型响应结果
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-01 20:25
 */
@Data
public class StatsListResult implements Serializable {
    /**
     * 查询结果，0-成功，1-失败
     */
    private int code = 0;
    /**
     * 查询信息
     */
    private String msg = "查询成功！";
    /**
     * 查询结果
     */
    private JSONArray data;
    /**
     * 合计行数据
     */
    private JSONObject totalRow;
    /**
     * 页面展示的字段列表
     */
    private List<String> showColumn = new ArrayList<>();
    /**
     * 点击详情时的分组方式
     */
    private String detailGroupBy;

}
