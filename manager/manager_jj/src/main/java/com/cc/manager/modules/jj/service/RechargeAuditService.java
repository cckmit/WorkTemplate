package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.config.CmTool;
import com.cc.manager.modules.jj.config.Config;
import com.cc.manager.modules.jj.config.SignatureAlgorithm;
import com.cc.manager.modules.jj.config.XMLHandler;
import com.cc.manager.modules.jj.entity.AllCost;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.entity.UserApp;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.AllCostMapper;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import com.cc.manager.modules.jj.mapper.UserAppMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.cc.manager.modules.jj.config.CmTool.createNonceStr;

/**
 * 提现审核
 *
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class RechargeAuditService extends BaseStatsService<Recharge, RechargeMapper> {

    private RechargeMapper rechargeMapper;
    private AllCostService allCostService;
    private UserAppService userAppService;
    private WxConfigService wxConfigService;

    private RechargeService rechargeService;
    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<Recharge> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<Recharge> entityList, StatsListResult statsListResult) {
        return null;
    }

    @Override
    public StatsListResult getPage(StatsListParam statsListParam) {
        StatsListResult statsListResult = new StatsListResult();
        // 判断请求参数是否为空
        if (StringUtils.isNotBlank(statsListParam.getQueryData())) {
            statsListParam.setQueryObject(JSONObject.parseObject(statsListParam.getQueryData()));
        }
        if (Objects.isNull(statsListParam.getQueryObject())) {
            statsListParam.setQueryObject(new JSONObject());
        }
        // 初始化查询的起止日期
        this.updateBeginAndEndDate(statsListParam);
        String beginDate = statsListParam.getQueryObject().getString("beginDate");
        String endDate = statsListParam.getQueryObject().getString("endDate");
        String  uid = statsListParam.getQueryObject().getString("uid");
        String userName = statsListParam.getQueryObject().getString("userName");
        String  productName = statsListParam.getQueryObject().getString("productName");
        String ddStatus = statsListParam.getQueryObject().getString("ddStatus");
        try {
            List<Recharge> recharges = this.rechargeService.selectAllRechargeAudit(beginDate, endDate);
            List<Recharge> rechargeList = new ArrayList<>();
            for (Recharge recharge : recharges) {
                String ddUid = recharge.getDdUid();
                if (StringUtils.isNotBlank(uid)) {
                    if (!uid.equals(ddUid)) {
                        continue;
                    }
                }
                if (StringUtils.isNotBlank(userName)) {
                    if (!userName.equals(recharge.getUserName())) {
                        continue;
                    }
                }
                if (StringUtils.isNotBlank(productName)) {
                    if (!productName.equals(recharge.getDdAppId())) {
                        continue;
                    }
                }
                if (StringUtils.isNotBlank(ddStatus)) {
                    if (recharge.getDdStatus() != (Integer.parseInt(ddStatus))) {
                        continue;
                    }
                }
                LocalDateTime times = recharge.getDdTimes();
                String ddTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(times);
                AllCost allCost = allCostService.selectCurrentCoin(ddTime);
                if (allCost != null) {
                    Long rmbCurrent = allCost.getDdCurrent();
                    //剩余金额
                    recharge.setRemainAmount(rmbCurrent.intValue() / 100);
                } else {
                    recharge.setRemainAmount(0);
                }
                int cashOutCurrent = rechargeMapper.selectCashOut(ddUid, ddTime);
                //已提现金额
                recharge.setRmbOut(new BigDecimal(cashOutCurrent));
                Integer programType = recharge.getProgramType();
                if (programType == 1 || programType == 2) {
                    rechargeList.add(recharge);
                }
            }
            statsListResult.setData(JSONArray.parseArray(JSON.toJSONString(rechargeList)));
            statsListResult.setCount(rechargeList.size());
        } catch (Exception e) {
            statsListResult.setCode(1);
            statsListResult.setMsg("查询结果异常，请联系开发人员！");
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return statsListResult;
    }

    /**
     * 初始化查询起止时间
     *
     * @param statsListParam 请求参数
     */
    private void updateBeginAndEndDate(StatsListParam statsListParam) {
        String beginDate = null, endDate = null;
        String times = statsListParam.getQueryObject().getString("times");
        if (StringUtils.isNotBlank(times)) {
            String[] timeRangeArray = StringUtils.split(times, "~");
            beginDate = timeRangeArray[0].trim();
            endDate = timeRangeArray[1].trim();
        }
        statsListParam.getQueryObject().put("beginDate", beginDate);
        statsListParam.getQueryObject().put("endDate", endDate);
    }

    @Autowired
    public void setRechargeMapper(RechargeMapper rechargeMapper) {
        this.rechargeMapper = rechargeMapper;
    }
    @Autowired
    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @Autowired
    public void setAllCostService(AllCostService allCostService) {
        this.allCostService = allCostService;
    }

    @Autowired
    public void setUserAppService(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    /**
     * 审核提现操作
     *
     * @param parameter parameter
     * @return PostResult
     */
    public PostResult auditRecharge(JSONArray parameter) {
        PostResult postResult = new PostResult();
        int error = 0;
        int errorCount = 0;
        for (int i = 0; i < parameter.size(); i++) {
            String ddId = parameter.getString(i);
            Recharge recharge = this.rechargeMapper.selectById(ddId);
            Integer programType = recharge.getProgramType();
            if (programType == 1) {
                UserApp userApp = this.userAppService.selectUserOpenId(recharge.getDdUid(), recharge.getDdAppId());
                String openId = userApp != null ? userApp.getDdOId() : "";
                BigDecimal ddRmb = recharge.getDdRmb().multiply(new BigDecimal("100"));
                WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, recharge.getDdAppId());
                LOGGER.info("当前订单提现金额：" + ddRmb.intValue() / 100 + "元, 用户Uid ：" + recharge.getDdUid() + " ,产品名 : " + wxConfig.getProductName());
                Map<String, String> backCharge = recharge(recharge.getDdId(), ddRmb.intValue(), wxConfig, openId, wxConfig.getProductName() + "-赛事奖金提现" + ddRmb.intValue() / 100 + "元", Config.SPBILL_CREATE_IP);
                String returnCode = backCharge != null ? backCharge.get("return_code") : null;
                if (StringUtils.equals(returnCode, "FAIL")) {
                    String returnMsg = backCharge != null ? backCharge.get("return_msg") : null;
                    recharge.setDdTip(returnMsg);
                    recharge.setDdStatus(2);
                    recharge.setDdTrans(LocalDateTime.now());
                    error = rechargeMapper.updateBySelective(recharge);
                    errorCount = errorCount + error;
                    LOGGER.info("提现返回结果 :" + returnCode);
                } else if ("SUCCESS".equals(returnCode)) {
                    String resultCode = backCharge.get("result_code");
                    if (StringUtils.equals(resultCode, "SUCCESS")) {
                        recharge.setDdStatus(200);
                        recharge.setDdTip("提现成功");
                        recharge.setDdTrans(LocalDateTime.now());
                        rechargeMapper.updateBySelective(recharge);
                    } else if (StringUtils.equals(resultCode, "FAIL")) {
                        String errCode = backCharge.get("err_code");
                        recharge.setDdTip(errCode);
                        recharge.setDdStatus(2);
                        recharge.setDdTrans(LocalDateTime.now());
                        error = rechargeMapper.updateBySelective(recharge);
                        errorCount = errorCount + error;
                        LOGGER.info("提现返回结果 :" + returnCode);
                    }
                }
            }
        }
        if (errorCount > 0) {
            postResult.setCode(2);
            postResult.setMsg("审核提现有误，请联系开发人员！");
        }
        return postResult;
    }

    /**
     * 提现相关接口
     * 企业付款到零钱
     *
     * @param orderId  订单编号
     * @param amount   提现金额
     * @param wxConfig 提现绑定appId
     * @param openid   提现用户
     * @param desc     提现描述
     * @return 提现结果
     */
    private Map<String, String> recharge(String orderId, int amount, WxConfig wxConfig, String openid, String desc, String ip) {
        Map<String, String> signMap = new HashMap<>();
        signMap.put("mch_appid", wxConfig.getId());
        signMap.put("mchid", wxConfig.getDdMchId());
        if (StringUtils.isNotBlank(Config.DEVICE_INFO)) {
            signMap.put("device_info", Config.DEVICE_INFO);
        }
        signMap.put("nonce_str", createNonceStr());
        signMap.put("partner_trade_no", orderId);
        signMap.put("openid", openid);
        signMap.put("check_name", Config.CHECK_NAME.name());
        signMap.put("re_user_name", "default");
        signMap.put("amount", String.valueOf(amount));
        if (StringUtils.isNotBlank(desc)) {
            signMap.put("desc", desc);
        } else {
            signMap.put("desc", Config.DESC);
        }
        signMap.put("spbill_create_ip", ip);
        SignatureAlgorithm algorithm = new SignatureAlgorithm(wxConfig.getDdKey(), signMap);
        String xml = algorithm.getSignXml();
        try {
            String result = CmTool.sendHttps(xml, Config.TRANSFERS_URL, RechargeService.class.getResource("/").getPath() + "static/" + wxConfig.getDdP12(), wxConfig.getDdP12Password());
            XMLHandler handler = XMLHandler.parse(result);
            return handler.getXmlMap();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

}
