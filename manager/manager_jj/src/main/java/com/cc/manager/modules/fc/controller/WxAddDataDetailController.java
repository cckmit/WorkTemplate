package com.cc.manager.modules.fc.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.fc.entity.MinitjWx;
import com.cc.manager.modules.fc.service.WxAddDataDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-13
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/wxAddDataDetail")
public class WxAddDataDetailController implements BaseCrudController {

    private WxAddDataDetailService wxAddDataDetailService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.wxAddDataDetailService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.wxAddDataDetailService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.wxAddDataDetailService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.wxAddDataDetailService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.wxAddDataDetailService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.wxAddDataDetailService.delete(requestParam);
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONArray getSelectArray(@PathVariable String requestParam) {
        return this.wxAddDataDetailService.getSelectArray(MinitjWx.class, requestParam);
    }

    @Autowired
    public void setWxAddDataDetailService(WxAddDataDetailService wxAddDataDetailService) {
        this.wxAddDataDetailService = wxAddDataDetailService;
    }

}

