package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.WxConfigService;
import com.fish.service.cache.CacheService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 产品信息
 * WxConfigController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class WxConfigController {

    @Autowired
    WxConfigService wxConfigService;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    CacheService cacheService;

    /**
     * 查询产品信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxconfig")
    public GetResult getWxConfig(GetParameter parameter) {
        return wxConfigService.findAll(parameter);
    }

    /**
     * 获取资源图
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/wxconfig/flushpicture")
    public PostResult flushResource(@RequestBody JSONObject parameter) {
        PostResult result = new PostResult();
        int i = wxConfigService.flushResource(parameter);
        if (i != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("wx_config", baseConfig.getFlushCache());
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 新增产品信息
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/wxconfig/new")
    public PostResult insertWxConfig(@RequestBody WxConfig productInfo) {
        PostResult result = new PostResult();
        int count = wxConfigService.insert(productInfo);
        if (count != 0) {
            //刷新业务表结构
            String resWx = ReadJsonUtil.flushTable("wx_config", baseConfig.getFlushCache());
            String resApp = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 修改产品信息
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/wxconfig/edit")
    public PostResult modifyWxConfig(@RequestBody WxConfig productInfo) {
        PostResult result = new PostResult();
        int count = wxConfigService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            // 刷新缓存，微信可加可不加
            this.cacheService.updateWxConfig(productInfo);
            //刷新业务表结构
            String resWx = ReadJsonUtil.flushTable("wx_config", baseConfig.getFlushCache());
            String resApp = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }


    /**
     * 根据appId获取产品信息
     *
     * @param ddAppId
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxconfig/get")
    public WxConfig selectWxConfig(String ddAppId) {
        return cacheService.getWxConfig(ddAppId);
    }
}
