package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.service.MiniGameService;
import com.cc.manager.modules.jj.entity.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 街机和fc全部app查询service
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-27 18:02
 */
@Service
public class JjAndFcAppConfigService {

    private WxConfigService wxConfigService;
    private MiniGameService miniGameService;

    /**
     * @return
     */
    public LinkedHashMap<String, String> getAllAppNameMap() {
        LinkedHashMap<String, String> allAppNameMap = new LinkedHashMap<>();
        List<WxConfig> wxConfigEntityList = this.wxConfigService.getCacheEntityList(WxConfig.class);
        List<MiniGame> miniGameEntityList = this.miniGameService.getCacheEntityList(MiniGame.class);
        wxConfigEntityList.forEach(wxConfig -> {
            allAppNameMap.put(wxConfig.getCacheKey(), wxConfig.getCacheValue());
        });
        miniGameEntityList.forEach(miniGame -> {
            if (!allAppNameMap.containsKey(miniGame.getCacheKey())) {
                allAppNameMap.put(miniGame.getCacheKey(), miniGame.getCacheValue());
            }
        });
        return allAppNameMap;
    }

    /**
     *
     * @return
     */
    public LinkedHashMap<String, JSONObject> getAllAppMap() {
        LinkedHashMap<String, JSONObject> allAppMap = new LinkedHashMap<>();
        List<WxConfig> wxConfigEntityList = this.wxConfigService.getCacheEntityList(WxConfig.class);
        List<MiniGame> miniGameEntityList = this.miniGameService.getCacheEntityList(MiniGame.class);
        wxConfigEntityList.forEach(wxConfig -> {
            JSONObject jsonObject = new JSONObject();
            // 平台
            jsonObject.put("platform", wxConfig.getDdAppPlatform());
            // 类型
            jsonObject.put("programType", wxConfig.getProgramType());
            // 名称
            jsonObject.put("name", wxConfig.getProductName());
            allAppMap.put(wxConfig.getCacheKey(), jsonObject);
        });
        miniGameEntityList.forEach(miniGame -> {
            if (!allAppMap.containsKey(miniGame.getCacheKey())) {
                JSONObject jsonObject = new JSONObject();
                // 平台
                jsonObject.put("platform", miniGame.getGameAppPlatform());
                // 类型
                jsonObject.put("programType", 0);
                // 名称
                jsonObject.put("name", miniGame.getGameName());
                allAppMap.put(miniGame.getCacheKey(), jsonObject);
            }
        });
        return allAppMap;
    }

    /**
     * @param requestParam
     * @return
     */
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        JSONArray selectOptionArray = new JSONArray();
        this.getAllAppNameMap().forEach((appId, appName) -> {
            JSONObject selectObject = new JSONObject();
            selectObject.put("key", appId);
            selectObject.put("value", appName);
            selectOptionArray.add(selectObject);
        });

        JSONObject resultObject = new JSONObject();
        resultObject.put("code", 1);
        resultObject.put("data", selectOptionArray);
        return resultObject;
    }

    @Autowired
    public void setWxConfigService(WxConfigService wxConfigService) {
        this.wxConfigService = wxConfigService;
    }

    @Autowired
    public void setMiniGameService(MiniGameService miniGameService) {
        this.miniGameService = miniGameService;
    }

}
