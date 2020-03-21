package com.fish.controller;

import com.fish.dao.second.model.ConfigSubscribe;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigSubscribeService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 订阅配置
 * ConfigSubscribeController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigSubscribeController {

    @Autowired
    ConfigSubscribeService configSubscribeService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询订阅信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configSubscribe")
    public GetResult getConfigSubscribe(GetParameter parameter) {
        return configSubscribeService.findAll(parameter);
    }

    /**
     * 新增
     *
     * @param configSubscribe
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configSubscribe/new")
    public PostResult insertConfigSubscribe(@RequestBody ConfigSubscribe configSubscribe) {
        PostResult result = new PostResult();
        int count = configSubscribeService.insert(configSubscribe);
        if (count == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("config_subscribe", baseConfig.getFlushCache());
            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 更新
     *
     * @param configSubscribe
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configSubscribe/edit")
    public PostResult modifyConfigSubscribe(@RequestBody ConfigSubscribe configSubscribe) {
        PostResult result = new PostResult();
        int count = configSubscribeService.updateByPrimaryKeySelective(configSubscribe);
        if (count != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("config_subscribe", baseConfig.getFlushCache());
            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

}
