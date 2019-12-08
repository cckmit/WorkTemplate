package com.fish.service;

import com.fish.dao.primary.mapper.StatisticsUserWeekMapper;
import com.fish.dao.primary.model.StatisticsUserWeek;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import com.fish.utils.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsUserWeekService implements BaseService<StatisticsUserWeek>
{
    @Autowired
    StatisticsUserWeekMapper userWeekMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("newWeek");
        parameter.setOrder("desc");
    }

    @Override
    public Class<StatisticsUserWeek> getClassInfo()
    {
        return StatisticsUserWeek.class;
    }

    @Override
    public boolean removeIf(StatisticsUserWeek statisticsUserWeek, Map<String, String> searchData)
    {
        try
        {
            String times = searchData.get("times");
            if (times == null || times.trim().isEmpty())
                return false;
            int[] weeks = XwhTool.parseWeek(times);
            int week = statisticsUserWeek.getNewWeek();
            return week < weeks[0] || week > weeks[1];
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return false;
    }

    @Override
    public List<StatisticsUserWeek> selectAll(GetParameter parameter)
    {
        return userWeekMapper.selectAll();
    }
}
