package com.cc.manager;

import com.cc.manager.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManagerJjApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    void contextLoads() {
        redisUtil.set("myName", "cc");
        System.out.println(redisUtil.get("myName"));
    }

}
