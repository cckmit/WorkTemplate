package com.cc.manager.modules.tt.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.tt.service.TtDMessageNoticeService;
import com.cc.manager.modules.tt.service.TtDailyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @date 2020-08-06
 */
@CrossOrigin
@RestController
@RequestMapping("/tt/ttMessageNotice")
public class TTMessageNoticeController {

    private TtDMessageNoticeService ttMessageNoticeService;

    @PostMapping(value = "/getNoticeData")
    public PostResult getData(@RequestBody JSONObject jsonObject) {
        return this.ttMessageNoticeService.getNoticeData(jsonObject);
    }

    @Autowired
    public void setTtMessageNoticeService(TtDMessageNoticeService ttMessageNoticeService) {
        this.ttMessageNoticeService = ttMessageNoticeService;
    }
}
