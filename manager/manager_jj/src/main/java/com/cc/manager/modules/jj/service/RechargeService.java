package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.AllCost;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class RechargeService extends BaseStatsService<Recharge, RechargeMapper> {

    private UserInfoService userInfoService;
    private AllCostService allCostService;
    private JjAndFcAppConfigService jjAndFcAppConfigService;

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Recharge> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Recharge> entityList, StatsListResult statsListResult) {
        return null;
    }

    /**
     * 查询提现记录数据
     *
     * @param beginDate beginDate
     * @param endDate   endDate
     * @return List
     */
    public List<Recharge> selectAllChargeRecord(String beginDate, String endDate) {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), "DATE(ddTimes)", beginDate, endDate);
        queryWrapper.orderByDesc("ddTrans");
        // 获取街机和FC的全部app信息
        LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
        List<Recharge> allRechargeList = this.list(queryWrapper);
        for (Recharge recharge : allRechargeList) {
            String ddUid = recharge.getDdUid();
            LocalDateTime ddTimes = recharge.getDdTimes();
            //获取当前剩余可提现金额
            AllCost allCost = allCostService.selectRemainAmount(ddUid, ddTimes);
            recharge.setRemainAmount(allCost == null ? 0 : allCost.getDdCurrent().intValue());
            //获取用户昵称
            UserInfo userInfoByUuid = this.userInfoService.getUserInfoByUuid(ddUid);
            recharge.setUserName(StringUtils.isNotBlank(userInfoByUuid.getDdName()) ? userInfoByUuid.getDdName() : "");
            //获取产品名称及产品类型
            JSONObject appObject = getAllAppMap.get(recharge.getDdAppId());
            recharge.setProductName(appObject != null ? appObject.getString("name") : "");
            recharge.setProgramType(appObject != null ? Integer.parseInt(appObject.getString("programType")) : 0);

        }
        return allRechargeList;
    }

    /**
     * 查询提现待审核数据
     *
     * @param beginDate beginDate
     * @param endDate   endDate
     * @return List
     */
    public List<Recharge> selectAllRechargeAudit(String beginDate, String endDate) {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.between(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate), "DATE(ddTimes)", beginDate, "endDate");
        queryWrapper.ne("ddStatus", 200).orderByDesc("ddTimes");
        // 获取街机和FC的全部app信息
        LinkedHashMap<String, JSONObject> getAllAppMap = this.jjAndFcAppConfigService.getAllAppMap();
        List<Recharge> allRechargeList = this.list(queryWrapper);
        for (Recharge recharge : allRechargeList) {
            UserInfo userInfoByUuid = this.userInfoService.getUserInfoByUuid(recharge.getDdUid());
            recharge.setUserName(userInfoByUuid.getDdName());
            JSONObject appObject = getAllAppMap.get(recharge.getDdAppId());
            recharge.setProductName(appObject != null ? appObject.getString("name") : "");
            recharge.setProgramType(appObject != null ? Integer.parseInt(appObject.getString("programType")) : 0);
        }

        return allRechargeList;
    }

    /**
     * 查询用户当前时间的已提现金额
     *
     * @param ddUid  ddUid
     * @param ddTime ddTime
     * @return BigDecimal
     */
    public BigDecimal selectUserCashOut(String ddUid, String ddTime) {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("ddTimes", ddTime);
        queryWrapper.eq(StringUtils.isNotBlank(ddUid), "ddUid", ddUid).eq("ddStatus", 200);
        queryWrapper.select("SUM(ddRmb) AS rmbOut");
        Map<String, Object> map = this.mapper.selectMaps(queryWrapper).get(0);
        if (MapUtils.isNotEmpty(map)) {
            return new BigDecimal(String.valueOf(map.get("rmbOut")));
        } else {
            return new BigDecimal(0);
        }
    }

    /**
     * 用户已提现金额查询
     *
     * @return Map
     */
    public List<Recharge> selectAllUserRecharged() {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ddStatus", 200).groupBy("ddUid");
        queryWrapper.select("ddUid", "SUM(ddRmb) as ddRmb");
        return this.list(queryWrapper);
    }

    /**
     * 根据ID查询当前提现记录
     *
     * @param ddId ddId
     * @return Recharge
     */
    public Recharge selectById(String ddId) {
        return this.mapper.selectById(ddId);
    }

    /**
     * 更新当前选中提现数据
     *
     * @param recharge recharge
     * @return int
     */
    public int updateBySelective(Recharge recharge) {
        if (this.updateById(recharge)) {
            return 1;
        }
        return 0;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setAllCostService(AllCostService allCostService) {
        this.allCostService = allCostService;
    }

    @Autowired
    public void setJjAndFcAppConfigService(JjAndFcAppConfigService jjAndFcAppConfigService) {
        this.jjAndFcAppConfigService = jjAndFcAppConfigService;
    }

}
