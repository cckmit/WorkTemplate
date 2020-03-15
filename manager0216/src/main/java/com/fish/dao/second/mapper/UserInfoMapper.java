package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserAllInfo;
import com.fish.dao.second.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper
{
    UserInfo selectByPrimaryKey(Integer id);

    UserInfo selectByDdUid(String uid);

    List<UserInfo> selectUserInfo(String uids);

    List<UserInfo> selectByDdName(String ddName);

    List<UserAllInfo> selectAll();

    List<UserAllInfo> selectByRegister(@Param("ddname") String ddname, @Param("dduid") String dduid,@Param("ddoid") String ddoid);

}