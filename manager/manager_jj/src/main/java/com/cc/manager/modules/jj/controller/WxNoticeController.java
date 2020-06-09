package com.cc.manager.modules.jj.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.WxNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信公告查询
 *
 * @author cf
 * @since 2020-06-05
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/wxNotice")
public class WxNoticeController implements BaseStatsController {

    private WxNoticeService wxNoticeService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.wxNoticeService.getPage(statsListParam);
    }

    @Autowired
    public void setWxNoticeService(WxNoticeService wxNoticeService) {
        this.wxNoticeService = wxNoticeService;
    }

}

