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
public class StatsPageParam implements Serializable {
    /**
     * 查询参数，Json格式
     */
    private String queryData;
    /**
     * 由queryData解析得到的Json对象
     */
    private JSONObject queryObject;
    /**
     * 排序条件，Json格式字符串：{"id":"ASC","name":"DESC"} -> ORDER BY ID ASC, name DESC <br/>
     * 具体参考：https://mp.baomidou.com/guide/wrapper.html#orderby
     */
    private String orderBy;
    /**
     * 当前页
     */
    private int page;
    /**
     * 每页显示条数
     */
    private int limit;
}
