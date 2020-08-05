package com.blaze.common;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 客户端请求返回值
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 21:05
 */
@Setter
@Getter
public class PostResult implements Serializable {
    /**
     * 0-失败，1-成功
     */
    int code = 1;
    /**
     * 详情
     */
    String msg;
    /**
     * 返回结果
     */
    Serializable data;

    /**
     * 更新返回值数据
     *
     * @param code 结果
     * @param msg  信息
     */
    public void updateMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
