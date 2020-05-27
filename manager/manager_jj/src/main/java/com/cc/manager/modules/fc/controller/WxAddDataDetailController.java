package com.cc.manager.modules.fc.controller;


import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.fc.service.WxAddDataDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cf
 * @since 2020-05-13
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/fc/wxAddDataDetail")
public class WxAddDataDetailController implements BaseStatsController {

    private WxAddDataDetailService wxAddDataDetailService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.wxAddDataDetailService.getPage(statsListParam);
    }

    @Autowired
    public void setWxAddDataDetailService(WxAddDataDetailService wxAddDataDetailService) {
        this.wxAddDataDetailService = wxAddDataDetailService;
    }

}

