package com.cc.manager.modules.jj.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.config.JjConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PersieServer 工具类
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-15 17:05
 */
@Component
public class PersieServerUtils {

    private JjConfig jjConfig;

    /**
     * 刷新服务器数据表
     *
     * @param tableName 数据表名称
     * @return 刷新结果
     */
    public PostResult refreshTable(String tableName) {
        PostResult postResult = new PostResult();
        JSONObject tableObject = new JSONObject();
        tableObject.put("name", tableName);
        String refreshResult = HttpUtil.post(this.jjConfig.getFlushCache(), tableObject.toJSONString());
        if (StringUtils.isBlank(refreshResult) || !StringUtils.contains(refreshResult, "success")) {
            postResult.setCode(2);
            postResult.setMsg("操作失败：数据保存成功，但服务器缓存刷新失败！");
        }
        return postResult;
    }

    /**
     * 刷新公众号数据表
     *
     * @param tableName 数据表名称
     * @return 刷新结果
     */
    public PostResult refreshPublicTable(String tableName) {
        PostResult postResult = new PostResult();
        JSONObject tableObject = new JSONObject();
        tableObject.put("name", tableName);
        String refreshResult = HttpUtil.post(this.jjConfig.getFlushPublicCache(), tableObject.toJSONString());
        if (StringUtils.isBlank(refreshResult) || !StringUtils.contains(refreshResult, "success")) {
            postResult.setCode(2);
            postResult.setMsg("操作失败：数据保存成功，但公众号服务器缓存刷新失败！");
        }
        return postResult;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}
