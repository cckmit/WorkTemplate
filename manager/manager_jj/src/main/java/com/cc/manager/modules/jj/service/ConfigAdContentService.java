package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.common.result.PostResult;
import com.cc.manager.modules.jj.entity.ConfigAdContent;
import com.cc.manager.modules.jj.entity.ConfigAdType;
import com.cc.manager.modules.jj.mapper.ConfigAdContentMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-07 15:34
 */
@Service
@DS("jj")
public class ConfigAdContentService extends BaseCrudService<ConfigAdContent, ConfigAdContentMapper> {

    private ConfigAdTypeService configAdTypeService;

    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<ConfigAdContent> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String adType = queryObject.getString("adType");
            queryWrapper.eq(StringUtils.isNotBlank(adType), "ddAdType", adType);
            String targetAppId = queryObject.getString("targetAppId");
            queryWrapper.like(StringUtils.isNotBlank(targetAppId), "ddTargetAppId", targetAppId);
            String promoteAppId = queryObject.getString("promoteAppId");
            queryWrapper.like(StringUtils.isNotBlank(promoteAppId), "ddPromoteAppId", promoteAppId);
        }
    }

    @Override
    protected void rebuildSelectedEntity(ConfigAdContent entity) {
        if (entity.getAdType() != 0) {
            entity.setAdTypeName(this.configAdTypeService.getCacheValue(ConfigAdType.class, String.valueOf(entity.getAdType())));
        }
    }

    /**
     * 查询推广APP列表
     *
     * @return 推广APP列表
     */
    public JSONObject getTargetAppArray() {
        JSONObject resultObject = new JSONObject();
        QueryWrapper<ConfigAdContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT ddTargetAppId AS targetAppId, ddTargetAppName AS targetAppName");
        List<ConfigAdContent> list = this.list(queryWrapper);
        JSONArray jsonArray = new JSONArray();
        list.forEach(configAdContent -> {
            JSONObject targetAppObject = new JSONObject();
            targetAppObject.put("key", configAdContent.getTargetAppId());
            targetAppObject.put("value", configAdContent.getTargetAppId() + "-" + configAdContent.getTargetAppName());
            jsonArray.add(targetAppObject);
        });
        resultObject.put("data", jsonArray);
        resultObject.put("code", 1);
        return resultObject;
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<ConfigAdContent> deleteWrapper) {
        return false;
    }

    /**
     * 自动上传
     *
     * @param requestParam
     * @return
     */
    public PostResult uploadImageUrlByUpload(String requestParam) {
        PostResult postResult = new PostResult();
        try {
            JSONArray contentImageUrlArray = JSONArray.parseArray(requestParam);
            for (int i = 0; i < contentImageUrlArray.size(); i++) {
                JSONObject contentImageUrl = contentImageUrlArray.getJSONObject(i);
                UpdateWrapper<ConfigAdContent> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set(true, "ddImageUrl", contentImageUrl.getString("imageUrl"));
                updateWrapper.eq(true, "ddId", contentImageUrl.getString("id"));
                this.update(updateWrapper);
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            postResult.setCode(2);
            postResult.setMsg("图片上传成功，但更新到服务器失败，请手动更新！");
        }
        return postResult;
    }

    @Autowired
    public void setConfigAdTypeService(ConfigAdTypeService configAdTypeService) {
        this.configAdTypeService = configAdTypeService;
    }

}
