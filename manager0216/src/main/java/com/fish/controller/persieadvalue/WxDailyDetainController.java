package com.fish.controller.persieadvalue;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.persieadvalue.WxAdPosService;
import com.fish.service.persieadvalue.WxDailyDetainService;
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
public class WxDailyDetainController {
    @Autowired
    WxDailyDetainService wxDailyDetainService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxDailyDetain")
    public GetResult getWxDailyDetain(GetParameter getParameter) {
        return this.wxDailyDetainService.findAll(getParameter);
    }
}
