package com.cc.manager.common.result;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * CRUD操作中的单条数据响应结果
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-22 20:42
 */
@Data
public class CrudObjectResult implements Serializable {

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
    private JSONObject data;

}
