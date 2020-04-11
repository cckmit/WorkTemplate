package com.fish.controller;

import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.service.ProductDataService;
import com.fish.utils.BaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 产品数据详情
 * Fc_ProductDataController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class Fc_ProductDataController {
    private final static Logger logger = LoggerFactory.getLogger(Fc_ProductDataController.class);
    @Autowired
    ProductDataService fcProductDataService;
    @Autowired
    BaseConfig baseConfig;

    /**
     * 查询产品信息详情
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/productdata")
    public GetResult getProductData(GetParameter parameter) {
        return fcProductDataService.findAll(parameter);
    }

    /**
     * 搜索产品信息详情
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/productdata/search")
    public GetResult searchData(HttpServletRequest request, GetParameter parameter) {
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String productName = request.getParameter("ddAppId");
        return fcProductDataService.searchProductData(beginDate, endDate, productName, parameter);
    }


}
