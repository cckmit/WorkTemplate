package com.fish.service;

import com.fish.dao.primary.mapper.StatisticsRetentionMapper;
import com.fish.dao.primary.model.StatisticsRetention;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsRetentionService implements BaseService<StatisticsRetention>
{
    @Autowired
    StatisticsRetentionMapper retentionMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("newDate");
        parameter.setOrder("desc");
    }

    @Override
    public Class<StatisticsRetention> getClassInfo()
    {
        return StatisticsRetention.class;
    }

    @Override
    public boolean removeIf(StatisticsRetention statisticsRetention, Map<String, String> searchData)
    {
        String times = searchData.get("times");
        if (times == null || times.trim().isEmpty())
            return false;
        Date[] parse = XwhTool.parseDate(times);
        return statisticsRetention.getNewDate().before(parse[0]) || statisticsRetention.getNewDate().after(parse[1]);
    }

    @Override
    public List<StatisticsRetention> selectAll(GetParameter parameter)
    {
        return retentionMapper.selectAll();
    }
}
