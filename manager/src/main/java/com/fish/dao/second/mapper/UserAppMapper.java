package com.fish.dao.second.mapper;

import com.fish.dao.second.model.UserApp;

public interface UserAppMapper {
    int deleteByPrimaryKey(String ddoid);

    int insert(UserApp record);

    int insertSelective(UserApp record);

    UserApp selectByPrimaryKey(String dduid);

    int updateByPrimaryKeySelective(UserApp record);

    int updateByPrimaryKey(UserApp record);
}