package com.cc.manager.common.result;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * post/put/delete操作返回结果
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-04-22 20:42
 */
@Data
public class PostResult {

    /**
     * 执行结果代码，1-成功，2-失败，这里定义为1/2是为了同步layui的msg样式
     */
    private int code = 1;

    /**
     * 执行结果信息
     */
    private String msg = "操作成功！";

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
