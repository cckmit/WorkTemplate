package com.fish.controller.persieadvalue;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.persieadvalue.WxAdPosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * WxAdPosController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class WxAdPosController {
    @Autowired
    WxAdPosService wxAdPosService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxAdPos")
    public GetResult getWxAdPosData(GetParameter getParameter) {
        return this.wxAdPosService.findAll(getParameter);
    }
}
