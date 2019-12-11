package com.fish.controller;

import com.fish.dao.primary.model.Ranking;
import com.fish.dao.primary.model.RankingRecord;
import com.fish.dao.primary.model.ShowRanking;
import com.fish.dao.second.model.Orders;
import com.fish.dao.second.model.ShowOrders;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.OrdersService;
import com.fish.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class RankingController {



    @Autowired
    RankingService rankingService;
    //查询展示比赛结果
    @ResponseBody
    @GetMapping(value = "/ranking")
    public GetResult getRanking(GetParameter parameter) {
        return rankingService.findAll(parameter);
    }
    @ResponseBody
    @PostMapping(value = "/ranking/result")
    public GetResult getRanking(@RequestBody String productInfo) {
        GetResult result = new GetResult();
        List<Ranking> rankings = rankingService.selectResult(productInfo);
        result.setData(rankings);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }


    //新增比赛结果
    @ResponseBody
    @PostMapping(value = "/ranking/new")
    public PostResult insertRanking(@RequestBody Ranking productInfo) {
        PostResult result = new PostResult();

        int count = rankingService.insert(productInfo);
      // count =1;
        if (count == 1) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","matchday");
//            String res= HttpUtil.post("http://192.168.1.183:8081/persieDeamon/flush/logic", paramMap.toJSONString());
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

    //修改比赛结果
    @ResponseBody
    @PostMapping(value = "/ranking/edit")
    public PostResult modifyRanking(@RequestBody RankingRecord productInfo) {
        PostResult result = new PostResult();
      //  int count =1;
       int count = rankingService.updateByPrimaryKeySelective(productInfo);
        if (count != 0) {
//            JSONObject paramMap = new JSONObject();
//            paramMap.put("name","matchday");
//            String res= HttpUtil.post("http://192.168.1.183:8081/persieDeamon/flush/logic", paramMap.toJSONString());
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
