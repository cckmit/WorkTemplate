package com.cc.manager.modules.fc.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.service.TtAdClipboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 头条剪切板广告数据
 *
 * @author cf
 * @since 2020-07-21
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/fc/ttAdClipboard")
public class TtAdClipboardController implements BaseStatsController {

    private TtAdClipboardService ttAdClipboardService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.ttAdClipboardService.getPage(statsListParam);
    }

    @Autowired
    public void setTtAdClipboardService(TtAdClipboardService ttAdClipboardService) {
        this.ttAdClipboardService = ttAdClipboardService;
    }

}

