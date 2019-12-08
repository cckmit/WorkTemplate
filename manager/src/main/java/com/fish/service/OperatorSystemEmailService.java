package com.fish.service;

import com.fish.dao.second.mapper.ConfigEmailMapper;
import com.fish.dao.second.model.ConfigEmail;
import com.fish.protocols.GetParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OperatorSystemEmailService implements BaseService<ConfigEmail>
{
    @Autowired
    ConfigEmailMapper emailMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("udptype");
        parameter.setOrder("desc");
    }

    @Override
    public Class<ConfigEmail> getClassInfo()
    {
        return ConfigEmail.class;
    }

    @Override
    public boolean removeIf(ConfigEmail configEmail, Map<String, String> searchData)
    {
        return false;
    }

    @Override
    public List<ConfigEmail> selectAll(GetParameter parameter)
    {
        return emailMapper.selectAll();
    }
}
