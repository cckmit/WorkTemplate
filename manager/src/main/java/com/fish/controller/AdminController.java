package com.fish.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController
{
    @ResponseBody
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @ResponseBody
    @GetMapping("/select")
    public String select()
    {
        return "select";
    }

    @ResponseBody
    @GetMapping("/menu")
    public JSONArray menu()
    {
        JSONArray jsonArray = new JSONArray();
        int index = 0;
        {
            JSONObject parent = new JSONObject();
            parent.put("id", ++index);
            parent.put("parentId", 0);
            parent.put("name", "产品配置");
            jsonArray.add(parent);
            final int parentId = index;
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "产品信息");
                wxInput.put("url", "wxConfig.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "审核配置");
                wxInput.put("url", "appConfig.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "产品赛制");
                wxInput.put("url", "ProductFormat.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "赛制配置");
                wxInput.put("url", "FormatConfig.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "群赛制配置");
                wxInput.put("url", "GroupFormatConfig.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "全局配置");
                wxInput.put("url", "ArcadeGlobalconfig.html");
                jsonArray.add(wxInput);
            }
        }

        {
            JSONObject parent = new JSONObject();
            parent.put("id", ++index);
            parent.put("parentId", 0);
            parent.put("name", "合集配置");
            jsonArray.add(parent);
            final int parentId = index;
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "游戏列表");
                child.put("url", "gamesList.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "合集配置");
                child.put("url", "gameset.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "游戏表");
                child.put("url", "gameTable.html");
                jsonArray.add(child);
            }
        }
        {
            JSONObject parent = new JSONObject();
            parent.put("id", ++index);
            parent.put("parentId", 0);
            parent.put("name", "数据统计");
            jsonArray.add(parent);
            final int grandparentId = index;

            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "在线情况统计");
                child.put("url", "OnlineStatistics.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "订单查询");
                child.put("url", "orders.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "常规赛结果查询");
                child.put("url", " Ranking.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "群比赛结果查询");
                child.put("url", "  GroupRanking.html");
                jsonArray.add(child);
            }

            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "用户数据统计");
                child.put("url", "userData.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "提现情况");
                child.put("url", "recharge.html");
                jsonArray.add(child);
            }

            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", grandparentId);
                child.put("name", "上传素材");
                child.put("url", "UploadTemplate.html");
                jsonArray.add(child);
            }

        }
//        {
//            JSONObject parent = new JSONObject();
//            parent.put("id", ++index);
//            parent.put("parentId", 0);
//            parent.put("name", "运营配置");
//            jsonArray.add(parent);
//            final int parentId = index;
//            {
//                JSONObject child = new JSONObject();
//                child.put("id", ++index);
//                child.put("parentId", parentId);
//                child.put("name", "邮箱");
//                child.put("url", "");
//                jsonArray.add(child);
//            }
//            {
//                JSONObject child = new JSONObject();
//                child.put("id", ++index);
//                child.put("parentId", parentId);
//                child.put("name", "奖励转换工具");
//                child.put("url", "RewardConvertTool.html");
//                jsonArray.add(child);
//            }
//            {
//                JSONObject child = new JSONObject();
//                child.put("id", ++index);
//                child.put("parentId", parentId);
//                child.put("name", "JSON转换工具模板");
//                child.put("url", "ConvertJsonTemplate.html");
//                jsonArray.add(child);
//            }
//        }
        return jsonArray;
    }
}
