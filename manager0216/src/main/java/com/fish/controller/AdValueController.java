package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.AdValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 广告数据查询Controller
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-03-10 14:01
 */
@Controller
@RequestMapping(value = "/manage")
public class AdValueController {

    @Autowired
    AdValueService adValueService;

    /**
     * @param getParameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/adValue")
    public GetResult getConfigAdSource(GetParameter getParameter) {
        return this.adValueService.findAll(getParameter);
    }
}
