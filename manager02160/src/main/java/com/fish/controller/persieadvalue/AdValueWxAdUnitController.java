package com.fish.controller.persieadvalue;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.persieadvalue.AdValueWxAdUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 广告接口数据统计Controller
 *
 * @author Host-0311
 * @date 2020-03-31 15:25:30
 */
@Controller
@RequestMapping(value = "/manage")
public class AdValueWxAdUnitController {

    @Autowired
    private AdValueWxAdUnitService adValueWxAdUnitService;

    /**
     * 查询数据
     *
     * @param getParameter
     * @return
     */
    @ResponseBody
    @RequestMapping("/adValueWxAdUnit")
    public GetResult selectAll(GetParameter getParameter) {
        return adValueWxAdUnitService.findAll(getParameter);
    }

}
