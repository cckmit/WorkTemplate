package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 充值订单查询
 *
 * @author cf
 * @since 2020-05-13
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/orders")
public class OrdersController implements BaseStatsController {

    private OrdersService ordersService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return ordersService.getPage(statsListParam);
    }

    /**
     * 补发订单
     *
     * @param singleOrder singleOrder
     * @return PostResult
     */
    @ResponseBody
    @PostMapping(value = "/supplement")
    public PostResult supplementOrders(@RequestBody JSONObject singleOrder) {
        return ordersService.supplementOrders(singleOrder);
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

}

