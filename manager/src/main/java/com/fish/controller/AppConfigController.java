package com.fish.controller;

import com.fish.dao.second.model.AppConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.AppConfigService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 审核配置
 * AppConfigController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class AppConfigController {

    @Autowired
    AppConfigService appConfigService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询AppConfig信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/appconfig")
    public GetResult getAppConfig(GetParameter parameter) {
        return appConfigService.findAll(parameter);
    }

    /**
     * 新增
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/appconfig/new")
    public PostResult insertAppConfig(@RequestBody AppConfig productInfo) {
        PostResult result = new PostResult();
        int count = appConfigService.insert(productInfo);
        if (count == 1) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
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
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/appconfig/edit")
    public PostResult modifyAppConfig(@RequestBody AppConfig productInfo) {
        PostResult result = new PostResult();
        int count = appConfigService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
            //刷新业务表结构
            String res = ReadJsonUtil.flushTable("app_config", baseConfig.getFlushCache());
            result.setMsg("操作成功");
        } else {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * @param id 审核AppID
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/appconfig/get")
    public AppConfig getGameSets(String id) {
        return this.appConfigService.select(id);
    }
}
