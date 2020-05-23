package com.cc.manager;

import com.cc.manager.modules.jj.entity.Recharge;
import com.cc.manager.modules.jj.mapper.RechargeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ManagerJjApplicationTests {

    @Autowired
    RechargeMapper rechargeMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void getRecharge() {
        List<Recharge> recharges = rechargeMapper.selectAllCharge(null,null);
        for (Recharge recharge : recharges) {
            System.out.println(recharge.toString());
        }

    }

}
