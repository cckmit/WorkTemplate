package com.fish.controller.persieadvalue;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.persieadvalue.WxDailyVisitTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/manage")
public class WxDailyVisitTrendController {

    @Autowired
    private WxDailyVisitTrendService wxDailyVisitTrendService;

    @ResponseBody
    @RequestMapping(value = "/wxDailyVisitTrend/daily")
    public GetResult selectDaily(GetParameter parameter) {
        return wxDailyVisitTrendService.findAll(parameter);
    }

}
