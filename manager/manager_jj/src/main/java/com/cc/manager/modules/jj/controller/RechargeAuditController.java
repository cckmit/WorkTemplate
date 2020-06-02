package com.cc.manager.modules.jj.controller;


import com.alibaba.fastjson.JSONArray;
import com.cc.manager.common.mvc.BaseStatsController;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.service.RechargeAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cf
 * @since 2020-05-09
 */
@CrossOrigin
@RestController
@RequestMapping("/jj/rechargeAudit")
public class RechargeAuditController implements BaseStatsController {

    private RechargeAuditService rechargeAuditService;

    @Override
    @GetMapping(value = "/getPage")
    public StatsListResult getPage(StatsListParam statsListParam) {
        return this.rechargeAuditService.getPage(statsListParam);
    }

    /**
     * 审核提现接口
     *
     * @param parameter parameter
     * @return PostResult
     */
    @PostMapping(value = "/auditRecharge")
    public PostResult auditRecharge(@RequestBody JSONArray parameter) {

        return rechargeAuditService.auditRecharge(parameter);

    }

    @Autowired
    public void setRechargeAuditService(RechargeAuditService rechargeAuditService) {
        this.rechargeAuditService = rechargeAuditService;
    }

}

