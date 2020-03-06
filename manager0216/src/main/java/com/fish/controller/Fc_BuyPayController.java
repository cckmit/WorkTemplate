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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 买量支出
 * Fc_BuyPayController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class Fc_BuyPayController {

    @Autowired
    BaseConfig baseConfig;
    @Autowired
    BuyPayService buyPayService;

    /**
     * 查询买量信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/buypay")
    public GetResult getBuyPay(GetParameter parameter) {
        return buyPayService.findAll(parameter);
    }

    /**
     * 导入买量信息EXCEL
     *
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/buypay/uploadExcel")
    public JSONObject uploadExcel(@RequestParam("file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        try {
            String readPath = baseConfig.getExcelSave();
            String originalFilename = file.getOriginalFilename();
            File saveFile = new File(readPath, originalFilename);
            FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
            ReadExcel readExcel = new ReadExcel();
            readExcel.readFile(saveFile);
            jsonObject.put("context", readExcel.read(0));
            buyPayService.insertExcel(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObject.put("code", 200);
        return jsonObject;
    }

    /**
     * 买量信息搜索
     *
     * @param request
     * @param parameter
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/buypay/search")
    public GetResult searchBuyPay(HttpServletRequest request, GetParameter parameter) {
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String productName = request.getParameter("productName");
        // productDataService.searchData(beginDate,endDate,productName,parameter);
        return buyPayService.searchData(beginDate, endDate, productName, parameter);
    }

    /**
     * 新增买量
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/buypay/new")
    public PostResult insertBuyPay(@RequestBody BuyPay productInfo) {
        PostResult result = new PostResult();
        int count = buyPayService.insert(productInfo);
        // count =1;
        if (count == 0) {
            result.setSuccessed(false);
            result.setMsg("操作失败，请联系管理员");
        }
        return result;
    }

    /**
     * 修改买量信息
     *
     * @param productInfo
     * @return
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
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/buypay/delete")
    public PostResult deleteBuyPay(@RequestBody JSONObject jsonObject) {

        return this.buyPayService.deleteSelective(jsonObject);

    }
}
