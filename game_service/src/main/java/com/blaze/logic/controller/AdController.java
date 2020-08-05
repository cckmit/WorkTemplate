package com.blaze.logic.controller;

import com.alibaba.fastjson.JSONObject;
import com.blaze.common.PostResult;
import com.blaze.logic.service.AdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 广告接口
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-17 17:09
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/ad")
public class AdController {

    private static final Logger AdLog = LoggerFactory.getLogger("AdLog");

    private AdService adService;

    /**
     * 获取广告
     *
     * @param info 获取广告新
     * @return 广告内容
     */
    @PostMapping("/get")
    public PostResult get(@RequestBody JSONObject info) {
        return this.adService.get(info);
    }

    /**
     * 提交广告日志
     *th
     * @param info 日志信息
     * @return 提交结果
     */
    @PostMapping("/log")
    public PostResult log(@RequestBody JSONObject info) {
        // 记录广告提交日志
        AdLog.info("[PRIMARY]" + info.toJSONString());
        // 通知客户端数据记录成功
        return new PostResult();
    }

    @Autowired
    public void setAdService(AdService adService) {
        this.adService = adService;
    }
}
