package com.blaze.logic.controller;

import com.alibaba.fastjson.JSONObject;
import com.blaze.common.PostResult;
import com.blaze.logic.service.AdStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 广告策略
 *
 * @author cf
 */
@RestController
@RequestMapping(value = "/adStrategy")
public class AdStrategyController {

    @Autowired
    AdStrategyService adStrategyService;

    @PostMapping("/asyncAdStrategyData")
    public PostResult asyncAdStrategyData(@RequestBody JSONObject asyncInfo) {
        return adStrategyService.asyncAdStrategyData(asyncInfo);
    }
}
