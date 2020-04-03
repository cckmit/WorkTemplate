package com.fish.controller.persieadvalue;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.persieadvalue.WxDailyDetainService;
import com.fish.service.persieadvalue.WxDailySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * WxDailyDetainController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class WxDailySummaryController {
    @Autowired
    WxDailySummaryService wxDailySummaryService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxDailySummary")
    public GetResult getWxDailySummary(GetParameter getParameter) {
        return this.wxDailySummaryService.findAll(getParameter);
    }
}
