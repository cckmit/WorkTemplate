package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdCombination;
import com.cc.manager.modules.jj.service.ConfigAdCombinationService;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:41
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/configAdCombination")
public class ConfigAdCombinationController implements BaseCrudController {

    private ConfigAdCombinationService configAdCombinationService;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.configAdCombinationService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.configAdCombinationService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.configAdCombinationService.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.configAdCombinationService.post(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult postResult = this.configAdCombinationService.put(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult postResult = this.configAdCombinationService.delete(requestParam);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.configAdCombinationService.getSelectArray(ConfigAdCombination.class, null);
    }

    /**
     * 修改广告合集名称
     *
     * @param requestParam 请求参数
     * @return 修改结果
     */
    @PostMapping(value = "/updateName")
    public PostResult updateName(@RequestBody String requestParam) {
        return this.configAdCombinationService.updateName(requestParam);
    }

    /**
     * 获取广告合集下配置的广告位置列表
     *
     * @param id 广告合集ID
     * @return 广告位置列表
     */
    @GetMapping(value = "/getAdPositionList/{id}")
    public JSONArray getAdPositionList(@PathVariable String id) {
        return this.configAdCombinationService.getAdPositionList(id);
    }

    /**
     * 新增广告位置
     *
     * @param addAdPositionObject 新增广告位置
     * @return 新增结果
     */
    @PostMapping(value = "/addAdPosition")
    public PostResult addAdPosition(@RequestBody JSONObject addAdPositionObject) {
        PostResult postResult = this.configAdCombinationService.addPosition(addAdPositionObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 批量删除广告位置
     *
     * @param deleteAdPositionsObject 批量删除广告位置
     * @return 新增结果
     */
    @PostMapping(value = "/deleteAdPositions")
    public PostResult deleteAdPositions(@RequestBody JSONObject deleteAdPositionsObject) {
        PostResult postResult = this.configAdCombinationService.deleteAdPositions(deleteAdPositionsObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 更新广告位置策略
     *
     * @param updateStrategyObject 更新数据对象
     * @return 更新结果
     */
    @PostMapping(value = "/updateAdStrategy")
    public PostResult updateAdStrategy(@RequestBody JSONObject updateStrategyObject) {
        PostResult postResult = this.configAdCombinationService.updateAdStrategy(updateStrategyObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 新增广告位
     *
     * @param addAdSpaceObject 新增数据对象
     * @return 新增结果
     */
    @PostMapping(value = "/addAdSpace")
    public PostResult addAdSpace(@RequestBody JSONObject addAdSpaceObject) {
        PostResult postResult = this.configAdCombinationService.addAdSpace(addAdSpaceObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 批量删除广告位
     *
     * @param deleteAdSpacesObject 新增数据对象
     * @return 新增结果
     */
    @PostMapping(value = "/deleteAdSpaces")
    public PostResult deleteAdSpaces(@RequestBody JSONObject deleteAdSpacesObject) {
        PostResult postResult = this.configAdCombinationService.deleteAdSpaces(deleteAdSpacesObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 新增广告内容
     *
     * @param addAdContentObject 新增数据对象
     * @return 新增结果
     */
    @PostMapping(value = "/addAdContent")
    public PostResult addAdContent(@RequestBody JSONObject addAdContentObject) {
        PostResult postResult = this.configAdCombinationService.addAdContent(addAdContentObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 批量删除广告位
     *
     * @param deleteAdContentsObject 新增数据对象
     * @return 删除结果
     */
    @PostMapping(value = "/deleteAdContents")
    public PostResult deleteAdContents(@RequestBody JSONObject deleteAdContentsObject) {
        PostResult postResult = this.configAdCombinationService.deleteAdContents(deleteAdContentsObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 保存广告位序号
     *
     * @param saveAdContentIndexObject 数据保存对象
     * @return 删除结果
     */
    @PostMapping(value = "/saveAdContentIndex")
    public PostResult saveAdContentIndex(@RequestBody JSONObject saveAdContentIndexObject) {
        PostResult postResult = this.configAdCombinationService.saveAdContentIndex(saveAdContentIndexObject);
        if (postResult.getCode() == 1) {
            postResult = this.persieServerUtils.refreshTable("config_ad_combination");
        }
        return postResult;
    }

    /**
     * 获取广告合集-广告位置下的广告位列表
     *
     * @param id 广告合集ID
     * @return 广告位置列表
     */
    @GetMapping(value = "/getAdSpaceList/{id}/{adPositionId}")
    public JSONObject getAdSpaceList(@PathVariable String id, @PathVariable String adPositionId) {
        return this.configAdCombinationService.getAdSpaceList(id, adPositionId);
    }

    /**
     * 获取广告合集-广告位置-广告位下的广告内容列表
     *
     * @param id 广告合集ID
     * @return 广告位置列表
     */
    @GetMapping(value = "/getAdContentList/{id}/{adPositionId}/{adSpaceId}")
    public JSONArray getAdContentList(@PathVariable String id, @PathVariable String adPositionId, @PathVariable String adSpaceId) {
        return this.configAdCombinationService.getAdContentList(id, adPositionId, adSpaceId);
    }

    @Autowired
    public void setConfigAdCombinationService(ConfigAdCombinationService configAdCombinationService) {
        this.configAdCombinationService = configAdCombinationService;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}
