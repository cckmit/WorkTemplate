package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.model.BuyPay;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.BuyPayService;
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
 * 买量支出
 * Fc_BuyPayController
 *
 * @author CF
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class Fc_BuyPayController {
    private static final Logger LOG = LoggerFactory.getLogger(Fc_BuyPayController.class);
    @Autowired
    BaseConfig baseConfig;

    @Autowired
    BuyPayService buyPayService;

    /**
     * 查询买量信息
     *
     * @param parameter parameter
     * @return 查询结果
     */
    @ResponseBody
    @GetMapping(value = "/buypay")
    public GetResult getBuyPay(GetParameter parameter) {
        return buyPayService.findAll(parameter);
    }

    /**
     * 导入买量信息EXCEL
     *
     * @param file file
     * @return 导入结果
     */
    @ResponseBody
    @PostMapping("/buypay/uploadExcel")
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
            buyPayService.insertExcel(jsonObject);
            jsonObject.put("code", 200);
        } catch (Exception e) {
            LOG.error("导入买量信息EXCEL失败" + ", 详细信息:{}", e.getMessage());
            jsonObject.put("code", 400);
        }
        return jsonObject;
    }

    /**
     * 买量信息搜索
     *
     * @param request   request
     * @param parameter parameter
     * @return 搜索结果
     */
    @ResponseBody
    @PostMapping(value = "/buypay/search")
    public GetResult searchBuyPay(HttpServletRequest request, GetParameter parameter) {
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String productName = request.getParameter("productName");
        return buyPayService.searchData(beginDate, endDate, productName, parameter);
    }

    /**
     * 新增买量
     *
     * @param productInfo productInfo
     * @return 新增结果
     */
    @ResponseBody
    @PostMapping(value = "/buypay/new")
    public PostResult insertBuyPay(@RequestBody BuyPay productInfo) {
        PostResult result = new PostResult();
        int count = buyPayService.insert(productInfo);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 修改买量信息
     *
     * @param productInfo productInfo
     * @return 修改结果
     */
    @ResponseBody
    @PostMapping(value = "/buypay/edit")
    public PostResult modifyBuyPay(@RequestBody BuyPay productInfo) {
        PostResult result = new PostResult();
        int count = buyPayService.updateByPrimaryKeySelective(productInfo);
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 删除买量信息
     *
     * @param jsonObject jsonObject
     * @return 删除结果
     */
    @ResponseBody
    @PostMapping(value = "/buypay/delete")
    public PostResult deleteBuyPay(@RequestBody JSONObject jsonObject) {
        return this.buyPayService.deleteSelective(jsonObject);
    }

}
