package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.second.model.ConfigAdApp;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ConfigAdAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 广告内容管理
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-28 20:40
 */
@Controller
@RequestMapping(value = "/manage")
public class ConfigAdAppController {

    @Autowired
    ConfigAdAppService adAppService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdApp")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adAppService.findAll(getParameter);
    }

    /**
     * 新增
     *
     * @param configAdApp
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdApp/new")
    public PostResult newConfigAdSource(@RequestBody ConfigAdApp configAdApp) {
        return this.adAppService.insert(configAdApp);
    }

    /**
     * 更新
     *
     * @param configAdApp
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdApp/edit")
    public PostResult updateConfigAdSource(@RequestBody ConfigAdApp configAdApp) {
        return this.adAppService.update(configAdApp);
    }

    /**
     * 复制
     *
     * @param configAdApp
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdApp/copy")
    public PostResult copyConfigAdSource(@RequestBody ConfigAdApp configAdApp) {
        return this.adAppService.copy(configAdApp);
    }

    /**
     * 删除
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdApp/delete")
    public PostResult delete(@RequestBody JSONObject jsonObject) {
        return this.adAppService.delete(jsonObject.getString("deleteIds"));
    }

    /**
     * @param id 微信广告配置ID
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/configAdApp/get")
    public ConfigAdApp getConfigAdApp(int id) {
        return this.adAppService.getConfigAdApp(id);
    }

    /**
     * 通过页面开关改变运营状态
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/configAdApp/change")
    public PostResult changeStatus(@RequestBody JSONObject jsonObject) {
        Integer id = jsonObject.getInteger("id");
        Boolean ddAllowedShow = jsonObject.getBoolean("ddAllowedShow");
        return this.adAppService.changeAllowedShowStatus(id, ddAllowedShow);
    }
}
