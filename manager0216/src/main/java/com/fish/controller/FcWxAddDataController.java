package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.WxAddDataService;
import com.fish.utils.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信广告数据明细
 * FcWxAddDataController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class FcWxAddDataController {
    @Autowired
    WxAddDataService wxAddDataService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询微信广告数据
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxAddData")
    public GetResult getProductData(GetParameter parameter) {
        return wxAddDataService.findAll(parameter);
    }

}
