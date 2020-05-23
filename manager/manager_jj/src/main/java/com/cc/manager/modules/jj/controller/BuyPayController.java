package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.BuyPay;
import com.cc.manager.modules.jj.service.BuyPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-08
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/buyPay")
public class BuyPayController implements BaseCrudController {

    private BuyPayService buyPayService;

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

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return this.buyPayService.getSelectArray(BuyPay.class,requestParam);
    }

    @Autowired
    public void setBuyPayService(BuyPayService buyPayService) {
        this.buyPayService = buyPayService;
    }

}

