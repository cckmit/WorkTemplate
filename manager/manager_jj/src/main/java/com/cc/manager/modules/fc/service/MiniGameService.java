package com.cc.manager.modules.fc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cc.manager.common.mvc.BaseCrudService;
import com.cc.manager.common.result.CrudPageParam;
import com.cc.manager.modules.fc.entity.MiniGame;
import com.cc.manager.modules.fc.mapper.MiniGameMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author cf
 * @since 2020-05-13
 */
@Service
@DS("fc")
public class MiniGameService extends BaseCrudService<MiniGame, MiniGameMapper> {
    @Override
    protected void updateGetPageWrapper(CrudPageParam crudPageParam, QueryWrapper<MiniGame> queryWrapper) {
        if (StringUtils.isNotBlank(crudPageParam.getQueryData())) {
            JSONObject queryObject = JSONObject.parseObject(crudPageParam.getQueryData());
            String times = queryObject.getString("times");
            if (StringUtils.isNotBlank(times)) {
                String[] timeRangeArray = StringUtils.split(times, "~");
                queryWrapper.between("DATE(wx_date)", timeRangeArray[0].trim(), timeRangeArray[1].trim());
            }
        }
    }

    @Override
    protected void rebuildSelectedEntity(MiniGame entity) {
        String platformName = "未知";
        switch (entity.getGameAppPlatform()) {
            case "weixin":
                platformName = "微信";
                break;
            case "q":
                platformName = "QQ";
                break;
            case "tt":
                platformName = "头条";
                break;
            default:
                break;
        }

        entity.setShowName("FC-" + platformName + "-小游戏-" + entity.getGameName());
        super.rebuildSelectedEntity(entity);
    }

    @Override
    protected boolean delete(String requestParam, UpdateWrapper<MiniGame> deleteWrapper) {
        return false;
    }

}
