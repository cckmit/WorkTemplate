package com.blaze;

import com.blaze.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class GameServiceApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        this.redisUtil.set("name", "cc");
        System.out.println(this.redisUtil.get("name"));

        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        this.redisUtil.hashPut("testHash", map);
        System.out.println(this.redisUtil.hasKey("testHash"));
    }

}
