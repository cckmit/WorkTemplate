package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.utils.ReadExcel;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.service.BuyPayService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

/**
 * 买量支出
 *
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/buyPay")
public class BuyPayController implements BaseCrudController {

    private BuyPayService buyPayService;
    private JjConfig jjConfig;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.buyPayService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.buyPayService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.buyPayService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.buyPayService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.buyPayService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.buyPayService.delete(requestParam);
    }


    /**
     * 导入买量数据
     *
     * @param file file
     * @return PostResult
     */
    @PostMapping(value = "/uploadExcel")
    public JSONObject updateShowId(@RequestParam("file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        Integer insertResult = 1;
        try {
            String readPath = jjConfig.getExcelSave();
            String originalFilename = file.getOriginalFilename();
            File saveFile = new File(readPath, Objects.requireNonNull(originalFilename));
            FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
            ReadExcel readExcel = new ReadExcel();
            readExcel.readFile(saveFile);
            jsonObject.put("context", readExcel.read(0));
            insertResult = buyPayService.insertExcel(jsonObject);
            jsonObject.put("code", insertResult);
        } catch (Exception e) {
            jsonObject.put("code", insertResult);
        }
        return jsonObject;
    }

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return this.buyPayService.getSelectArray(BuyPay.class, requestParam);
    }

    @Autowired
    public void setBuyPayService(BuyPayService buyPayService) {
        this.buyPayService = buyPayService;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

}

