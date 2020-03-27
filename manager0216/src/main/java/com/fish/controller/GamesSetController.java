package com.fish.controller;

import com.fish.dao.primary.model.ArcadeGameSet;
import com.fish.dao.second.model.WxConfig;
import com.fish.protocols.GetParameter;
import com.fish.protocols.GetResult;
import com.fish.protocols.PostResult;
import com.fish.service.GamesSetService;
import com.fish.service.WxConfigService;
import com.fish.utils.BaseConfig;
import com.fish.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合集配置
 * GamesSetController
 *
 * @author
 * @date
 */
@Controller
@RequestMapping(value = "/manage")
public class GamesSetController {
    @Autowired
    GamesSetService gamesSetService;

    @Autowired
    WxConfigService wxConfigService;

    @Autowired
    BaseConfig baseConfig;


    /**
     * 查询合集配置
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/gameset")
    public GetResult getGameSet(GetParameter parameter) {
        return gamesSetService.findAll(parameter);
    }

    /**
     * 查询合集信息下拉框信息
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/gamesetInfo")
    public List<ArcadeGameSet> getGameInfo(GetParameter parameter) {
        return gamesSetService.selectAll(parameter);
    }

    /**
     * 合集跳转关系
     *
     * @param parameter
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/gameset/jumpdirect")
    public List<WxConfig> getJumpDirect(GetParameter parameter) {
        List<WxConfig> wxConfigs = wxConfigService.selectAll(parameter);
        for (WxConfig wxConfig : wxConfigs) {
            String ddappid = wxConfig.getDdappid();
            if (StringUtils.isNotBlank((wxConfig.getProductName()))) {
                String productName = wxConfig.getProductName();
                wxConfig.setJumpDirect(productName + "-" + ddappid);
            } else {
                wxConfig.setJumpDirect(ddappid);
            }
        }
        return wxConfigs;
    }

    /**
     * 新增合集配置
     *
     * @param productInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/gameset/new")
    public PostResult insertGameSet(@RequestBody ArcadeGameSet productInfo) {
        PostResult result = new PostResult();
        int count = gamesSetService.insert(productInfo);
        switch (count) {
            case 1:
                //刷新业务表结构
                String res = ReadJsonUtil.flushTable("gameset", baseConfig.getFlushCache());
                break;
            case 5:
                result.setSuccessed(false);
                result.setMsg("操作失败，游戏代号重复");
                break;
            default:
                result.setSuccessed(false);
                result.setMsg("操作失败，请联系管理员");
                break;
        }
        return result;
    }

    /**
     * 修改合集信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/gameset/edit")
    public PostResult modifyGameSet(@RequestBody ArcadeGameSet productInfo) {
        PostResult result = new PostResult();
        int count = gamesSetService.updateByPrimaryKeySelective(productInfo);
        switch (count) {
            case 1:
                //刷新业务表结构
                String res = ReadJsonUtil.flushTable("gameset", baseConfig.getFlushCache());
                break;
            case 5:
                result.setSuccessed(false);
                result.setMsg("操作失败，游戏代号重复");
                break;
            default:
                result.setSuccessed(false);
                result.setMsg("操作失败，请联系管理员");
                break;
        }
        return result;
    }

}
