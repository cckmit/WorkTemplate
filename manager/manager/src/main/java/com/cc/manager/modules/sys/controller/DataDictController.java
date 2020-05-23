package com.cc.manager.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.manager.common.mvc.BaseCrudController;
import com.cc.manager.common.result.GetPageParam;
import com.cc.manager.common.result.GetPageResult;
import com.cc.manager.common.result.GetResult;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.sys.entity.DataDict;
import com.cc.manager.modules.sys.service.DataDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-03 23:07
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/dataDict")
public class DataDictController implements BaseCrudController {

    private DataDictService dataDictService;

    @Override
    @GetMapping(value = "/id/{id}")
    public GetResult getObjectById(@PathVariable String id) {
        return this.dataDictService.getObjectById(id);
    }

    @Override
    @GetMapping(value = "/getObject/{getObjectParam}")
    public GetResult getObject(@PathVariable String getObjectParam) {
        return this.dataDictService.getObject(getObjectParam);
    }

    @Override
    @GetMapping(value = "/getPage")
    public GetPageResult getPage(GetPageParam getPageParam) {
        return this.dataDictService.getPage(getPageParam);
    }

    @Override
    @PostMapping
    public PostResult post(@RequestBody String requestParam) {
        return this.dataDictService.post(requestParam);
    }

    @Override
    @PutMapping
    public PostResult put(@RequestBody String requestParam) {
        return this.dataDictService.put(requestParam);
    }

    @Override
    @DeleteMapping
    public PostResult delete(@RequestBody String requestParam) {
        return this.dataDictService.delete(requestParam);
    }

    /**
     * 获取数据列表
     *
     * @return 查询列表
     */
    @GetMapping(value = "/getDataTypeArray")
    public JSONArray getDataTypeArray() {
        List<DataDict> list = this.dataDictService.getCacheValueList(DataDict.class);
        List<String> dataTypeList = new ArrayList<>();
        list.forEach(dataDict -> {
            if (!dataTypeList.contains(dataDict.getDataType())) {
                dataTypeList.add(dataDict.getDataType());
            }
        });
        return JSONArray.parseArray(JSON.toJSONString(dataTypeList));
    }

    /**
     * 获取某个类型的数据列表，用于下拉框和赋值
     *
     * @return 数据列表
     */
    @GetMapping(value = "/getDataArrayByType")
    public JSONArray getDataArrayByType(String dataType) {
        JSONArray returnArray = new JSONArray();
        if (StringUtils.isNotBlank(dataType)) {
            List<DataDict> list = this.dataDictService.getCacheValueList(DataDict.class);
            list.forEach(dataDict -> {
                if (StringUtils.equals(dataType, dataDict.getDataType())) {
                    JSONObject dictObject = new JSONObject();
                    dictObject.put("dataKey", dataDict.getDataKey());
                    dictObject.put("dataValue", dataDict.getDataValue());
                    returnArray.add(dictObject);
                }
            });
        }
        return returnArray;
    }

    @Autowired
    public void setDataDictService(DataDictService dataDictService) {
        this.dataDictService = dataDictService;
    }

}
