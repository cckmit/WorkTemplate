package com.cc.manager.common.result;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据统计表返回值
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-01 20:25
 */
@Data
public class GetStatsPageResult implements Serializable {
    /**
     * 查询结果，0-成功，1-失败
     */
    private int code = 0;
    /**
     * 查询信息
     */
    private String msg = "";
    /**
     * 数据总量
     */
    private long count;
    /**
     * 查询结果
     */
    private JSONArray data;
    /**
     * 页面展示的字段列表
     */
    private List<String> showColumn = new ArrayList<>();
    /**
     * 点击详情额外携带的字段参数，为空表示查询后不再有下一级详情
     */
    private String extraColumn;

    /**
     * 点击详情额外的数据分组字段
     */
    private String extraGroupBy;

}
