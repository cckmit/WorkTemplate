package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信和FC所有app查询接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-25 16:53
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/jjAndFcAppConfig")
public class JjAndFcAppConfigController {

    private JjAndFcAppConfigService jjAndFcAppConfigService;

    /**
     * 查询街机和fc的应用列表，如果存在相同的appId，以街机为准
     *
     * @param requestParam 请求参数
     * @return 数据结果
     */
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.jjAndFcAppConfigService.getSelectArray(requestParam);
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

}
