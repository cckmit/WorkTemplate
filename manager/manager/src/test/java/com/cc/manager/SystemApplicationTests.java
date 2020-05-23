package com.cc.manager;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.modules.sys.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SystemApplicationTests {

    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword("tn6i7ImGw3BJ");
        user.setNickName("管理员");
        user.setRoleIds("#");
        System.out.println(user);
        String userStr = JSONObject.toJSONString(user);
        JSONObject userObject = JSONObject.parseObject(userStr);
        userObject.put("password", "tn6i7ImGw3BJ");
        System.out.println(userObject);
        user = JSONObject.parseObject(userObject.toJSONString(), User.class);
        System.out.println(user);

        HttpRequest httpRequest = HttpUtil.createRequest(Method.PUT, "http://localhost:10000/manager_system/user/");
        httpRequest.body(userObject.toJSONString());
        HttpResponse httpResponse = httpRequest.execute();
        System.out.println(httpResponse.body());
    }

    @Test
    public void testPost() {

    }

}
