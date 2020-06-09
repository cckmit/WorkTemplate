package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.utils.RedisUtil;
import com.cc.manager.modules.jj.entity.SupplementOrder;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.entity.UserValue;
import com.cc.manager.modules.jj.entity.WxConfig;
import com.cc.manager.modules.jj.mapper.SupplementOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author cf
 * @since 2020-05-09
 */
@Service
@DS("jj")
public class SupplementOrderService extends BaseCrudService<SupplementOrder, SupplementOrderMapper> {

    private WxConfigService wxConfigService;
    private UserInfoService userInfoService;
    private UserValueService userValueService;
    private RedisUtil redisUtil;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<SupplementOrder> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(create_time)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
            String uid = queryObject.getString("uid");
            queryWrapper.like(StringUtils.isNotBlank(uid), "userId", uid);
            String name = queryObject.getString("name");
            queryWrapper.like(StringUtils.isNotBlank(name), "userName", name);
        }
        queryWrapper.orderByDesc("create_time");

    }

    /**
     * 如有需要，根据提交的数据更新插入数据库实体对象
     *
     * @param requestParam 请求参数
     * @param entity       数据对象
     */
    @Override
    protected void updateInsertEntity(String requestParam, SupplementOrder entity) {
        UserValue userValue = new UserValue();
        String userId = entity.getUserId();
        userValue.setDdUid(userId);
        Integer coinCount = entity.getCoinCount();
        UserValue userValues = this.userValueService.getById(userId);
        // 查询用户当前账户金额
        userValue.setDdCoinCount(coinCount + userValues.getDdCoinCount());
        UserInfo userInfo = this.userInfoService.getUserInfoByUuid(userId);
        // 获取用户昵称
        String ddName = userInfo.getDdName();
        // 昵称设置
        entity.setUserName(StringUtils.isNotBlank(ddName) ? ddName : "");
        // 产品信息处理
        WxConfig wxConfig = this.wxConfigService.getCacheEntity(WxConfig.class, entity.getAppId());
        if (wxConfig != null) {
            entity.setAppName(StringUtils.isNotBlank(wxConfig.getProductName()) ? wxConfig.getProductName() : "");
            entity.setProgramType(wxConfig.getProgramType());
        }
        // 处理redis客户端实时金币数量
        Object coin = redisUtil.hashGet("user-" + userId, "coin");
        LOGGER.info("获取redis客户端实时金币数量：" + coin.toString());
        coinCount = coinCount + (Integer) coin;
        boolean redisUpdate = redisUtil.hashPut("user-" + userId, "coin", coinCount);
        if (redisUpdate) {
            entity.setCreateTime(LocalDateTime.now());
            // 更新UserValue的用户账户数据
            UpdateWrapper<UserValue> userValueUpdate = new UpdateWrapper<>();
            userValueUpdate.eq("ddUid", userId);
            Object coinCurrent = redisUtil.hashGet("user-" + userId, "coin");
            // 获取补单后的redis实时金币进行更新数据库
            userValue.setDdCoinCount((Integer) coinCurrent);
            this.userValueService.update(userValue, userValueUpdate);
        } else {
            entity = null;
        }
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<SupplementOrder> deleteWrapper) {
        return false;
    }

    public SupplementOrder selectCurrentCoin(String uid) {
        SupplementOrder supplementOrder = new SupplementOrder();
        // 查询当前用户信息
        UserInfo userInfo = this.userInfoService.getUserInfoByUuid(uid);
        if (userInfo != null) {
            UserValue cacheUserValue = this.userValueService.getById(userInfo.getDdUid());
            userInfo.setDdCoinCount(cacheUserValue.getDdCoinCount());
            supplementOrder.setUserId(userInfo.getDdUid());
            supplementOrder.setCurrentCoin(userInfo.getDdCoinCount().longValue());
            supplementOrder.setAppId(userInfo.getDdAppId());
            supplementOrder.setUserName(userInfo.getDdName());
        }
        return supplementOrder;
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
    public void setUserValueService(UserValueService userValueService) {
        this.userValueService = userValueService;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

}
