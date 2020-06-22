package com.cc.manager.modules.jj.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.CrudPageResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdCombination;
import com.cc.manager.modules.jj.service.ConfigAdCombinationService2;
import com.cc.manager.modules.jj.utils.PersieServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-15 20:35
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/jj/configAdCombination3")
public class ConfigAdCombinationController2 implements BaseCrudController {

    private ConfigAdCombinationService2 configAdCombinationService2;
    private PersieServerUtils persieServerUtils;

    @Override
    @GetMapping(value = "/id/{id}")
    public CrudObjectResult getObjectById(@PathVariable String id) {
        return this.configAdCombinationService2.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public CrudObjectResult getObject(@PathVariable String getObjectParam) {
        return this.configAdCombinationService2.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public CrudPageResult getPage(CrudPageParam crudPageParam) {
        return this.configAdCombinationService2.getPage(crudPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        PostResult postResult = this.configAdCombinationService2.post(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        PostResult postResult = this.configAdCombinationService2.put(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        PostResult postResult = this.configAdCombinationService2.delete(requestParam);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    @Override
    @GetMapping(value = "/getSelectArray/{requestParam}")
    public JSONObject getSelectArray(@PathVariable String requestParam) {
        return this.configAdCombinationService2.getSelectArray(ConfigAdCombination.class, null);
    }

    /**
     * 修改广告合集名称
     *
     * @param requestParam 请求参数
     * @return 修改结果
     */
    @PostMapping(value = "/updateName")
    public PostResult updateName(@RequestBody String requestParam) {
        return this.configAdCombinationService2.updateName(requestParam);
    }


    @GetMapping(value = "/getAdPositionTable/{id}")
    public JSONObject getAdPositionTable(@PathVariable String id) {
        return this.configAdCombinationService2.getAdPositionTable(id);
    }

    @GetMapping(value = "/getAdContentTable/{id}")
    public JSONObject getAdContentTable(@PathVariable String id) {
        return this.configAdCombinationService2.getAdContentTable(id);
    }

    /**
     * 新增广告位置
     *
     * @param addAdPositionObject 新增广告位置
     * @return 新增结果
     */
    @PostMapping(value = "/addAdPosition")
    public PostResult addAdPosition(@RequestBody JSONObject addAdPositionObject) {
        PostResult postResult = this.configAdCombinationService2.addPosition(addAdPositionObject);
//        if (postResult.getCode() == 1) {
//             this.persieServerUtils.refreshTable("config_ad_combination");
//        }
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
        PostResult postResult = this.configAdCombinationService2.addAdContent(addAdContentObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    /**
     * 批量删除广告位置
     *
     * @param deleteAdPositionsObject 批量删除广告位置
     * @return 新增结果
     */
    @DeleteMapping(value = "/deleteAdPosition")
    public PostResult deleteAdPosition(@RequestBody JSONObject deleteAdPositionsObject) {
        PostResult postResult = this.configAdCombinationService2.deleteAdPosition(deleteAdPositionsObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    /**
     * 批量删除广告位
     *
     * @param deleteAdContentsObject 新增数据对象
     * @return 删除结果
     */
    @DeleteMapping(value = "/deleteAdContent")
    public PostResult deleteAdContent(@RequestBody JSONObject deleteAdContentsObject) {
        PostResult postResult = this.configAdCombinationService2.deleteAdContent(deleteAdContentsObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    /**
     * 保存广告位序号
     *
     * @param saveAdContentOrderNumObject 数据保存对象
     * @return 删除结果
     */
    @PostMapping(value = "/saveAdContentOrderNum")
    public PostResult saveAdContentOrderNum(@RequestBody JSONObject saveAdContentOrderNumObject) {
        PostResult postResult = this.configAdCombinationService2.saveAdContentOrderNum(saveAdContentOrderNumObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    @PostMapping(value = "/copyPosition")
    public PostResult copyPosition(@RequestBody JSONObject combinationIdObject) {
        PostResult postResult = this.configAdCombinationService2.copyPosition(combinationIdObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }
    @PostMapping(value = "/copyContent")
    public PostResult copyContent(@RequestBody JSONObject combinationIdObject) {
        PostResult postResult = this.configAdCombinationService2.copyContent(combinationIdObject);
//        if (postResult.getCode() == 1) {
//            this.persieServerUtils.refreshTable("config_ad_combination");
//        }
        return postResult;
    }

    @Autowired
    public void setConfigAdCombinationService2(ConfigAdCombinationService2 configAdCombinationService2) {
        this.configAdCombinationService2 = configAdCombinationService2;
    }

    @Autowired
    public void setPersieServerUtils(PersieServerUtils persieServerUtils) {
        this.persieServerUtils = persieServerUtils;
    }

}
