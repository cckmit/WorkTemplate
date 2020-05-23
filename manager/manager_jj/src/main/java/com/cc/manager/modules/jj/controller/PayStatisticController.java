package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.service.OrdersService;
import com.cc.manager.modules.jj.service.PayStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/payStatistic")
public class PayStatisticController implements BaseCrudController {
    private PayStatisticService payStatisticService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.payStatisticService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.payStatisticService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.payStatisticService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.payStatisticService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.payStatisticService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.payStatisticService.delete(requestParam);
    }

    @Override
    public JSONArray getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setPayStatisticService(PayStatisticService payStatisticService) {
        this.payStatisticService = payStatisticService;
    }
}

