package com.cc.manager.tt.manager_tt;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManagerTtApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void test() {
        JSONObject jsonOne = new JSONObject();
        JSONObject jsonTwo = new JSONObject();

        jsonOne.put("name", "kewen");
        jsonOne.put("age", "24");

        jsonTwo.put("name", "Dota");
        jsonTwo.put("hobbit2", "wow");

        JSONObject jsonThree = new JSONObject();

        jsonThree.putAll(jsonOne);
        jsonThree.putAll(jsonTwo);

        System.out.println(jsonThree.toString());
    }
    }


