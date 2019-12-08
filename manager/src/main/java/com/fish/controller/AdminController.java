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
            parent.put("name", "微信官方数据");
            jsonArray.add(parent);
            final int parentId = index;
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "访问趋势");
                wxInput.put("url", "WxAccess.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "分享趋势");
                wxInput.put("url", "WxShare.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "用户留存趋势");
                wxInput.put("url", "WxRetained.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "总收入趋势");
                wxInput.put("url", "WxAllTotal.html");
                jsonArray.add(wxInput);
            }
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", parentId);
                wxInput.put("name", "数据录入");
                wxInput.put("url", "WxInput.html");
                jsonArray.add(wxInput);
            }
        }

        {
            {
                JSONObject wxInput = new JSONObject();
                wxInput.put("id", ++index);
                wxInput.put("parentId", 0);
                wxInput.put("name", "基础数据统计");
                jsonArray.add(wxInput);
            }
            final int baseIndex = index;
            {
                JSONObject parent = new JSONObject();
                parent.put("id", ++index);
                parent.put("parentId", baseIndex);
                parent.put("name", "针对用户统计");
                jsonArray.add(parent);
                final int parentId = index;
                {
                    JSONObject child = new JSONObject();
                    child.put("id", ++index);
                    child.put("parentId", parentId);
                    child.put("name", "新增 & 活跃");
                    child.put("url", "statistics_user.html");
                    jsonArray.add(child);
                }
                {
                    JSONObject child = new JSONObject();
                    child.put("id", ++index);
                    child.put("parentId", parentId);
                    child.put("name", "留存 & 流失");
                    child.put("url", "statistics_user_retention.html");
                    jsonArray.add(child);
                }
                {
                    JSONObject child = new JSONObject();
                    child.put("id", ++index);
                    child.put("parentId", parentId);
                    child.put("name", "收入 & 成本");
                    child.put("url", "statistics_pay.html");
                    jsonArray.add(child);
                }
            }
//            {
//                JSONObject wxInput = new JSONObject();
//                wxInput.put("id", ++index);
//                wxInput.put("parentId", 0);
//                wxInput.put("name", "针对游戏统计");
//                jsonArray.add(wxInput);
//            }
        }
        {
            JSONObject wxInput = new JSONObject();
            wxInput.put("id", ++index);
            wxInput.put("parentId", 0);
            wxInput.put("name", "核心数据记录");
            jsonArray.add(wxInput);
            final int baseIndex = index;
            {
                {
                    JSONObject parent = new JSONObject();
                    parent.put("id", ++index);
                    parent.put("parentId", baseIndex);
                    parent.put("name", "用户详情");
                    parent.put("url", "BasicUserInfo.html");
                    jsonArray.add(parent);
                    final int parentId = index;
                    {
                        JSONObject child = new JSONObject();
                        child.put("id", ++index);
                        child.put("parentId", parentId);
                        child.put("name", "鲸鱼用户");
                        child.put("url", "BasicPayUser.html");
                        jsonArray.add(child);
                    }
                }
                {
                    JSONObject parent = new JSONObject();
                    parent.put("id", ++index);
                    parent.put("parentId", baseIndex);
                    parent.put("name", "充值详情");
                    parent.put("url", "BasicUserPayInfo.html");
                    jsonArray.add(parent);
                }
                {
                    JSONObject parent = new JSONObject();
                    parent.put("id", ++index);
                    parent.put("parentId", baseIndex);
                    parent.put("name", "图鉴详情");
                    parent.put("url", "BasicUserBookInfo.html");
                    jsonArray.add(parent);
                }
            }
        }
        {
            JSONObject parent = new JSONObject();
            parent.put("id", ++index);
            parent.put("parentId", 0);
            parent.put("name", "运营配置");
            jsonArray.add(parent);
            final int parentId = index;
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "邮箱");
                child.put("url", "OperatorEmail.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "奖励转换工具");
                child.put("url", "RewardConvertTool.html");
                jsonArray.add(child);
            }
            {
                JSONObject child = new JSONObject();
                child.put("id", ++index);
                child.put("parentId", parentId);
                child.put("name", "JSON转换工具模板");
                child.put("url", "ConvertJsonTemplate.html");
                jsonArray.add(child);
            }
        }
        return jsonArray;
    }
}
