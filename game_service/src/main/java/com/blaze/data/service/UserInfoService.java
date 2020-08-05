package com.blaze.data.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blaze.data.entity.UserInfo;
import com.blaze.data.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-06-09 20:34
 */
@Service
public class UserInfoService extends BaseService<UserInfo, UserInfoMapper> {

    /**
     * 通过openId查询对应用户
     *
     * @param openId openId
     * @return 用户信息
     */
    public UserInfo getUserInfoByOpenId(String openId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
        queryWrapper.eq("open_id", openId).last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

}
