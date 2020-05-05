package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.UserInfo;

import java.util.List;

public interface UserInfoMapper {

    UserInfo selectByDdUid(String uid);

    List<UserInfo> selectUserInfo(String uids);

    List<UserInfo> selectByDdName(String ddName);

    List<UserAllInfo> selectAll();

    List<UserAllInfo> selectAllUserInfo(String ddname, String dduid, String ddoid);

    UserAllInfo selectUserCoin(String uid);
}