package com.cc.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.utils.RedisUtil;
import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.entity.UserApp;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import com.cc.manager.modules.jj.mapper.UserAppMapper;
import com.cc.manager.modules.jj.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ManagerJjApplicationTests {

    @Autowired
    UserAppMapper userAppMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Test
    void contextLoads() {
    }

    @Test
    public void getString() {
        QueryWrapper<UserApp> userAppQueryWrapper = new QueryWrapper<>();
        userAppQueryWrapper.eq("ddUid", "6F8BE96F678D2BCAA5653BA58E20EB96").eq("ddAppId", 1110381534);
        UserApp userApp = userAppMapper.selectOne(userAppQueryWrapper);
        System.out.println(userApp.toString());
    }

    @Test
    public void getUserInfo() {

        Integer coin = (Integer) redisUtil.hashGet("user-oxDM75DNVEcHIT0eHnL-QYCMv8sY", "coin");
        System.out.println(coin.toString());
//        UserInfo userInfo = this.userInfoService.getById("oSn_Lw5rxqPXgC3b_M0heLW5wzbA");
//        System.out.println(userInfo.toString()

    }
    @Test
    public void getRecharge() {
//        List<Recharge> recharges = rechargeMapper.selectAllCharge(null,null);
//        for (Recharge recharge : recharges) {
//            System.out.println(recharge.toString());
//        }

    }

}
