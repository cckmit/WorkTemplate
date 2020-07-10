package com.cc.manager.modules.tt.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudObjectResult;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.utils.ReadExcel;
import com.cc.manager.modules.jj.config.JjConfig;
import com.cc.manager.modules.tt.entity.TtValueMapping;
import com.cc.manager.modules.tt.mapper.TtValueMappingMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author cf
 * @since 2020-07-09
 */
@Service
public class TtValueMappingService extends BaseCrudService<TtValueMapping, TtValueMappingMapper> {

    private JjConfig jjConfig;

    /**
     * 导入映射关系
     *
     * @param file file
     */
    public JSONObject uploadExcel(MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        Integer insertResult = 1;
        try {
            String readPath = jjConfig.getExcelSave();
            String originalFilename = file.getOriginalFilename();
            File saveFile = new File(readPath, Objects.requireNonNull(originalFilename));
            FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
            ReadExcel readExcel = new ReadExcel();
            readExcel.readFile(saveFile);
            jsonObject.put("context", readExcel.read(0));
            insertResult = this.insertExcel(jsonObject);
            jsonObject.put("code", insertResult);
        } catch (Exception e) {
            jsonObject.put("code", insertResult);
        }
        return jsonObject;
    }

    private Integer insertExcel(JSONObject record) {
        String context = record.getString("context");
        context = context.substring(1, context.length() - 1);
        try {
            JSONArray param = new JSONArray(Collections.singletonList(context));
            List<TtValueMapping> lists = new ArrayList<>();
            for (Object o : param) {
                String singleData = o.toString();
                String singleString = singleData.substring(1, singleData.length() - 1);
                String[] split = singleString.split("], ");
                for (int j = 0; j < split.length; j++) {
                    if (j != 0) {
                        String single = split[j].substring(1);
                        String[] singleSplit = single.split(",");
                        Map<String, String> mapSingle = new HashMap<>();
                        TtValueMapping ttValueMapping = new TtValueMapping();
                        for (int x = 0; x < singleSplit.length; x++) {
                            switch (x) {
                                case 0:
                                    mapSingle.put("groupByKey", singleSplit[x].trim());
                                    break;
                                case 1:
                                    mapSingle.put("groupByValue", singleSplit[x].trim());
                                    break;
                                default:
                                    break;
                            }
                        }
                        String groupByKey = mapSingle.get("groupByKey");
                        String groupByValue = mapSingle.get("groupByValue");

                        ttValueMapping.setGroupByKey(groupByKey);
                        ttValueMapping.setGroupByValue(groupByValue);
                        lists.add(ttValueMapping);

                    }
                }
                this.saveOrUpdateBatch(lists);
            }
        } catch (JSONException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 2;
        }
        return 1;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<TtValueMapping> entityList, int batchSize) {
        try {
            for (TtValueMapping ttValueMapping : entityList) {
                this.saveOrUpdate(ttValueMapping);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdate(TtValueMapping entity) {
        String groupByKey = entity.getGroupByKey();
        entity.setInsertTime(LocalDateTime.now());
        QueryWrapper<TtValueMapping> ttDailyValueQueryWrapper = new QueryWrapper<>();
        ttDailyValueQueryWrapper.eq("groupByKey", groupByKey);
        TtValueMapping tableContent = this.getOne(ttDailyValueQueryWrapper);
        //数据存在更新，不存在则新增
        if (tableContent != null) {
            UpdateWrapper<TtValueMapping> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("groupByKey", groupByKey);
            return this.update(entity, updateWrapper);
        } else {
            return this.save(entity);
        }
    }

    @Autowired
    public void setJjConfig(JjConfig jjConfig) {
        this.jjConfig = jjConfig;
    }

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<TtValueMapping> queryWrapper) {

    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<TtValueMapping> deleteWrapper) {
        return false;
    }

}
