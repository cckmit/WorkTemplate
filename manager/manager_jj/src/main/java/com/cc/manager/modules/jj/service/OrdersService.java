package com.cc.manager.modules.jj.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.utils.CmTool;
import com.cc.manager.common.utils.SignatureAlgorithm;
import com.cc.manager.common.utils.XMLHandler;
import com.cc.manager.common.utils.log4j.Log4j;
import com.cc.manager.config.BaseConfig;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.jj.entity.GoodsValueExt;
import com.cc.manager.modules.jj.entity.Orders;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.OrdersMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.cc.manager.common.utils.CmTool.createNonceStr;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("jj")
public class OrdersService extends BaseCrudService<Orders, OrdersMapper> {
    // 签名类型
    private static final String SIGN_TYPE = "MD5";
    private JjConfig jjConfig;
    private WxConfigService wxConfigService;
    private GoodsValueExtService goodsValueExtService;
    private UserInfoService userInfoService;

    private OrdersMapper ordersMapper;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<Orders> queryWrapper) {
        // 前端提交的条件
        JSONObject queryData = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
            String times = queryData.getString("times");
            String tradeNumber = queryData.getString("tradeNumber");
            String uid = queryData.getString("uid");
            String userName = queryData.getString("userName");
            String openID = queryData.getString("openID");
            String payState = queryData.getString("payState");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(ddTime)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            if (StringUtils.isNotBlank(tradeNumber)) {
                queryWrapper.like("ddId", tradeNumber);
            }
            if (StringUtils.isNotBlank(uid)) {
                queryWrapper.like("ddUid", uid);
            }
            if (StringUtils.isNotBlank(openID)) {
                queryWrapper.like("ddOId", openID);
            }
            if (StringUtils.isNotBlank(payState)) {
                queryWrapper.eq("DATE(ddState)", payState);
            }
        }
    }

    /**
     * 重构分页查询结果，比如进行汇总复制计算等操作
     *
     * @param crudPageParam 查询参数
     * @param entityList    查询数据对象列表
     */
    @Override
    protected void rebuildSelectedList(CrudPageParam crudPageParam, List<Orders> entityList) {
        // 前端提交的条件
        JSONObject queryData = null;
        String userName = null;
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            queryData = JSONObject.parseObject(crudPageParam.getQueryData());
        }
        if (queryData != null) {
             userName = queryData.getString("userName");
        }
        for (Orders order : entityList) {
            String ddAppId = order.getDdAppId();
            WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ddAppId);
            if (wxConfig != null) {
                String productName = wxConfig.getProductName();
                String originName = wxConfig.getOriginName();
                Integer programType = wxConfig.getProgramType();
                if (programType != null) {
                    order.setProductType(programType);
                }
                if (productName != null) {
                    order.setProductName(productName);
                }
                if (originName != null) {
                    order.setOriginName(originName);
                }
            }
            UserInfo userInfo = this.userInfoService.getCacheEntity(UserInfo.class, order.getDdUid());

            if (userInfo != null) {
                order.setUserName(userInfo.getDdName());
            }
            Integer goodId = order.getDdGId();
            GoodsValueExt goodsValueExt = this.goodsValueExtService.getCacheEntity(GoodsValueExt.class, goodId.toString());
            if (goodsValueExt != null) {
                String goodName = goodsValueExt.getDdName();
                String ddDesc = goodsValueExt.getDdDesc();
                order.setGoodsName(goodName);
                order.setDdDesc(ddDesc);
            }

        }
    /*    if(userName !=null){
            String finalUserName = userName;
            entityList.removeIf(entity->{return removeEntity(entity, finalUserName);});
        }*/
        //entityList.removeIf(entity->!this.userInfoService.getCacheEntity(UserInfo.class, entity.getDdUid()).getDdName().contains(userName));
    }

    private boolean removeEntity(Orders entity, String userName) {
      return   !this.userInfoService.getCacheEntity(UserInfo.class, entity.getDdUid()).getDdName().contains(userName);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<Orders> deleteWrapper) {
        return false;
    }


    @Override
    protected void updateInsertEntity(String requestParam, Orders entity) {

    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setGoodsValueExtService(GoodsValueExtService goodsValueExtService) {
        this.goodsValueExtService = goodsValueExtService;
    }

    @Autowired
    public void setOrdersMapper(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }


    /**
     * 补发订单
     *
     * @param appId   appId
     * @param uid     uid
     * @param orderId orderId
     * @return int
     */
    public int supplementOrders(String appId, String uid, String orderId) {
        //查询订单产品信息
        Orders order = ordersMapper.selectByPrimaryKey(orderId);
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, appId);
        String ddMchId = wxConfig.getDdMchId();
        Map<String, String> stringStringMap = searchPayOrder(appId, ddMchId, orderId);
        boolean status = orderIsSuccess(stringStringMap);
        LOGGER.info("补单状态status :" + status + "-appId :" + appId + "-orderId :" + orderId);
        if (status) {
            order.setDdTrans(null);
            ordersMapper.updateByPrimaryKey(order);
            String supplementUrl = this.jjConfig.getSupplementUrl();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", "2.2.2");
            jsonObject.put("appId", appId);
            jsonObject.put("uid", uid);
            jsonObject.put("orderid", orderId);
            //验单
            String result = HttpUtil.post(supplementUrl, jsonObject.toString());
            JSONObject jsonResult = JSONObject.parseObject(result);
            //订单状态
            if ("success".equals(jsonResult.getString("result"))) {
                String ddOrder = stringStringMap.get("transaction_id");
                order.setDdState(1);
                order.setDdOrder(ddOrder);
                ordersMapper.updateByPrimaryKeySelective(order);
                return 200;
            }
        }
        return 0;
    }

    /**
     * 查询微信支付订单
     *
     * @param orderId 订单号
     */
    private Map<String, String> searchPayOrder(String ddAppId, String ddMchId, String orderId) {

        // 查询订单在数据库中是否存在
        // 封装查询订单参数
        Map<String, String> searchOrderMap = new HashMap<>();
        searchOrderMap.put("appid", ddAppId);
        searchOrderMap.put("mch_id", ddMchId);
        searchOrderMap.put("out_trade_no", orderId);
        searchOrderMap.put("nonce_str", createNonceStr());
        searchOrderMap.put("sign_type", SIGN_TYPE);
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, ddAppId);
        SignatureAlgorithm signatureAlgorithm = new SignatureAlgorithm(wxConfig.getDdKey(), searchOrderMap);
        String searchOrderXml = signatureAlgorithm.getSignXml();
        try {
            // 从微信平台里查询支付订单
            String searchResultXml = CmTool.sendHttps(searchOrderXml, this.jjConfig.getWxQueryUrl(), OrdersService.class.getResource("/").getPath() + "static/" + wxConfig.getDdP12(), wxConfig.getDdP12Password());
            XMLHandler parse = XMLHandler.parse(searchResultXml);
            return parse.getXmlMap();
        } catch (Exception e) {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return null;
    }

    /**
     * 订单是否成功
     *
     * @param orderMap 订单回调内容
     * @return 结果
     */
    private boolean orderIsSuccess(Map<String, String> orderMap) {
        return existResult(orderMap, "result_code") && existResult(orderMap, "return_code") && existResult(orderMap, "trade_state");
    }

    /**
     * 检测是否匹配
     *
     * @param map 内容
     * @param key 查询参数
     * @return 是否匹配
     */
    private static boolean existResult(Map<String, String> map, String key) {
        String resultCode = map.get(key);
        return "success".equalsIgnoreCase(resultCode);
    }

}
