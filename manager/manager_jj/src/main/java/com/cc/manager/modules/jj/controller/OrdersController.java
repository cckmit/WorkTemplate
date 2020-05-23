package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.service.OrdersService;
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
@RequestMapping("/jj/orders")
public class OrdersController implements BaseCrudController {
    private OrdersService ordersService;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.ordersService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.ordersService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.ordersService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.ordersService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.ordersService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.ordersService.delete(requestParam);
    }
    /**
     * 补发订单
     *
     * @param singleOrder
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/supplement")
    public PostResult supplementOrders(@RequestBody JSONObject singleOrder) {
        PostResult result = new PostResult();
        int count = 0;
        String appId = singleOrder.getString("appId");
        String uid = singleOrder.getString("uid");
        String orderId = singleOrder.getString("orderid");
        count = ordersService.supplementOrders(appId, uid, orderId);
        if (count == 200) {
            result.setCode(1);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(2);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }

    }
    @Override
    public JSONArray getSelectArray(String requestParam) {
        return null;
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
}

