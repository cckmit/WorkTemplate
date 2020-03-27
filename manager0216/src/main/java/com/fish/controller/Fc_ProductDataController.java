package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.third.model.ProductData;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.ProductDataService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadExcel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Objects;

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

    /**
     * 上传小程序信息
     *
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/productdata/uploadExcel")
    public JSONObject uploadExcel(@RequestParam("file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        try {
            String readPath = baseConfig.getExcelSave();
            String originalFilename = file.getOriginalFilename();
            File saveFile = new File(readPath, Objects.requireNonNull(originalFilename));
            FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
            ReadExcel readExcel = new ReadExcel();
            readExcel.readFile(saveFile);
            jsonObject.put("context", readExcel.read(0));
            fcProductDataService.insertExcel(jsonObject);
        } catch (Exception e) {
            logger.error("上传小程序数据失败" + e.getMessage());
        }
        jsonObject.put("code", 200);
        return jsonObject;
    }

    /**
     * 新增小程序产品广告信息
     *
     * @param productData
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/productdata/new")
    public PostResult insertProductData(@RequestBody ProductData productData) {
        PostResult result = new PostResult();
        int count = fcProductDataService.insert(productData);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 修改小程序产品广告信息
     *
     * @param productData
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/productdata/edit")
    public PostResult modifyProductData(@RequestBody ProductData productData) {
        PostResult result = new PostResult();
        int count = fcProductDataService.updateByPrimaryKeySelective(productData);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 删除小程序产品广告信息
     *
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/productdata/delete")
    public PostResult deleteBuyPay(@RequestBody JSONObject jsonObject) {
        return this.fcProductDataService.delete(jsonObject);
    }
}
