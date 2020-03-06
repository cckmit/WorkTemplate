package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.ConfigAdContent;
import com.fish.dao.second.model.ConfigAdPosition;
import com.fish.dao.second.model.ConfigAdWx;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdContentService;
import com.fish.service.ConfigAdWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 广告内容管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 20:40
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigAdWxController {

    @Autowired
    ConfigAdWxService adWxService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdWx")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adWxService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdWx
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdWx/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdWx configAdWx) {
        return this.adWxService.insert(configAdWx);
    }

    /**
     * 更新
     *
     * @param configAdWx
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdWx/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdWx configAdWx) {
        return this.adWxService.update(configAdWx);
    }

    /**
     * 删除
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdWx/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        return this.adWxService.delete(jsonObject.getString("deleteIds"));
    }

    /**
     * @param id 微信广告配置ID
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdWx/get")
    public ConfigAdWx getConfigAdSpace(int id) { return this.adWxService.getConfigAdWx(id); }

}
