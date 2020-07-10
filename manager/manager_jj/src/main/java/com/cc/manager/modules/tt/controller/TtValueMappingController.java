package com.cc.manager.modules.tt.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.tt.service.TtDailyValueService;
import com.cc.manager.modules.tt.service.TtValueMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cf
 * @since 2020-07-09
 */
@CrossOrigin
@RestController
@RequestMapping("/tt/ttValueMapping")
public class TtValueMappingController implements BaseCrudController {

    private TtValueMappingService ttValueMappingService;

    /**
     * 导入映射关系
     *
     * @param file file
     * @return PostResult
     */
    @PostMapping(value = "/uploadExcel")
    public JSONObject uploadExcel(@RequestParam("file") MultipartFile file) {
        return this.ttValueMappingService.uploadExcel(file);

    }

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(String id) {
        return null;
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(String getObjectParam) {
        return null;
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return ttValueMappingService.getPage(crudPageParam);
    }

    @Override
    public PostResult post(String requestParam) {
        return null;
    }

    @Override
    public PostResult put(String requestParam) {
        return null;
    }

    @Override
    public PostResult delete(String requestParam) {
        return null;
    }

    @Override
    public JSONObject getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setTtValueMappingService(TtValueMappingService ttValueMappingService) {
        this.ttValueMappingService = ttValueMappingService;
    }
}

