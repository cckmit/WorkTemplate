package com.fish.controller;

import com.fish.dao.primary.model.Ranking;
import com.fish.dao.primary.model.RankingRecord;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GroupRankingService;
import com.fish.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/manage")
public class GroupRankingController {

    @Autowired
    GroupRankingService groupRankingService;
    //查询展示群比赛结果
    @ResponseBody
    @GetMapping(value = "/groupranking")
    public GetResult getGroupRanking(GetParameter parameter) {
        return groupRankingService.findAll(parameter);
    }
    @ResponseBody
    @PostMapping(value = "/groupranking/result")
    public GetResult getGroupRankingResult(@RequestBody String productInfo) {
        GetResult result = new GetResult();
        List<Ranking> rankings = groupRankingService.selectResult(productInfo);
        result.setData(rankings);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    //新增群比赛结果
    @ResponseBody
    @PostMapping(value = "/groupranking/new")
    public PostResult insertGroupRanking(@RequestBody Ranking productInfo) {
        PostResult result = new PostResult();

        int count = groupRankingService.insert(productInfo);
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

    //修改群比赛结果
    @ResponseBody
    @PostMapping(value = "/groupranking/edit")
    public PostResult modifyGroupRanking(@RequestBody RankingRecord productInfo) {
        PostResult result = new PostResult();
        //  int count =1;
        int count = groupRankingService.updateByPrimaryKeySelective(productInfo);
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
