package com.fish.service;

import com.fish.dao.second.mapper.UserInfoMapper;
import com.fish.dao.second.model.UserInfo;
import com.fish.protocols.GetParameter;
import com.fish.protocols.PlayUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BasicUserService implements BaseService<PlayUserVO>
{
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("userId");
        parameter.setOrder("desc");
    }

    @Override
    public Class<PlayUserVO> getClassInfo()
    {
        return PlayUserVO.class;
    }

    @Override
    public boolean removeIf(PlayUserVO playUserVO, Map<String, String> searchData)
    {
        if (existTimeFalse(playUserVO.getRegister(), searchData.get("times")))
            return true;
        if (existValueFalse(searchData.get("userId"), playUserVO.getUserId()))
            return true;
        if (existValueFalse(searchData.get("nickName"), playUserVO.getNickName()))
            return true;
        if (existValueFalse(searchData.get("platform"), playUserVO.getPlatform()))
            return true;
        return existValueFalse(searchData.get("device"), playUserVO.getDevice());
    }

    @Override
    public List<PlayUserVO> selectAll(GetParameter parameter)
    {
        return userInfoMapper.selectPlayUser();
    }

    public UserInfo selectByUserId(String userId)
    {
        return userInfoMapper.selectByPrimaryKey(Long.valueOf(userId));
    }
}
