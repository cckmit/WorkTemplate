package com.fish.controller;

import com.fish.dao.primary.model.GameDayInfo;
import com.fish.dao.primary.model.OnlineStatistics;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GameDayInfoService;
import com.fish.service.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class OnlineStatisticsController {

    @Autowired
    OnlineService onlineService;

    //查询在线情况
    @ResponseBody
    @GetMapping(value = "/online")
    public GetResult getOnline(GetParameter parameter) {

        GetResult onlineStatistics = onlineService.findAll(parameter);

        return onlineStatistics;
    }

    //新增产品赛制信息
    @ResponseBody
    @GetMapping(value = "/online/flush")
    public PostResult insertProductFormat(GetParameter parameter) {
        PostResult result = new PostResult();

        int count = onlineService.insert(parameter);
        if (count == 1) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","game_round");
//            String res= HttpUtil.post("https://sgame.qinyougames.com/persieDeamon/flush/logic", paramMap.toJSONString());
//            System.out.println("我是res返回值 : "+res);
            result.setCode(200);
            result.setMsg("操作成功");
            return result;
        } else {
            result.setCode(404);
            result.setMsg("操作失败，请联系管理员");
            return result;
        }
    }

}
