package com.cc.manager.modules.jj.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc.manager.common.mvc.BaseStatsService;
import com.cc.manager.common.result.StatsListParam;
import com.cc.manager.common.result.StatsListResult;
import com.cc.manager.modules.jj.entity.UserApp;
import com.cc.manager.modules.jj.mapper.UserAppMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cf
 * @since 2020-05-25
 */
@Service
public class UserAppService extends BaseStatsService<UserApp, UserAppMapper> {

    @Override
    protected void updateGetListWrapper(StatsListParam statsListParam, QueryWrapper<UserApp> queryWrapper, StatsListResult statsListResult) {

    }

    @Override
    protected JSONObject rebuildStatsListResult(StatsListParam statsListParam, List<UserApp> entityList, StatsListResult statsListResult) {
        return null;
    }

    /**
     * 获取用户OpenId
     *
     * @param ddUid   ddUid
     * @param ddAppId ddAppId
     * @return UserApp
     */
    public UserApp selectUserOpenId(String ddUid, String ddAppId) {
        QueryWrapper<UserApp> userAppQueryWrapper = new QueryWrapper<>();
        userAppQueryWrapper.eq("ddUid", ddUid).eq("ddAppId", ddAppId).last("LIMIT 1");
        return this.mapper.selectOne(userAppQueryWrapper);
    }

}
