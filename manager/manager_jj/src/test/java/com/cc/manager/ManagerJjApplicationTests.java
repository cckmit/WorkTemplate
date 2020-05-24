package com.cc.manager;

import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.entity.UserInfo;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import com.cc.manager.modules.jj.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ManagerJjApplicationTests {

    @Autowired
    RechargeMapper rechargeMapper;
    @Autowired
    UserInfoService userInfoService;
    @Test
    void contextLoads() {
    }

    @Test
    public void getUserInfo() {
        UserInfo userInfo = this.userInfoService.getById("oSn_Lw5rxqPXgC3b_M0heLW5wzbA");
        System.out.println(userInfo.toString()

        );
    }
    @Test
    public void getRecharge() {
        List<Recharge> recharges = rechargeMapper.selectAllCharge(null,null);
        for (Recharge recharge : recharges) {
            System.out.println(recharge.toString());
        }

    }

}
