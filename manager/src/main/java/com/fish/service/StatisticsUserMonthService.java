package com.fish.service;

import com.fish.dao.primary.mapper.StatisticsUserMonthMapper;
import com.fish.dao.primary.model.StatisticsUserMonth;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import com.fish.utils.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsUserMonthService implements BaseService<StatisticsUserMonth>
{
    @Autowired
    StatisticsUserMonthMapper userMonthMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("newMonth");
        parameter.setOrder("desc");
    }

    @Override
    public Class<StatisticsUserMonth> getClassInfo()
    {
        return StatisticsUserMonth.class;
    }

    @Override
    public boolean removeIf(StatisticsUserMonth statisticsUserMonth, Map<String, String> searchData)
    {
        try
        {
            String times = searchData.get("times");
            if (times == null || times.trim().isEmpty())
                return false;
            int[] months = XwhTool.parseMonth(times);
            return statisticsUserMonth.getNewMonth() < months[0] || statisticsUserMonth.getNewMonth() > months[1];
        } catch (Exception e)
        {
            LOGGER.error(Log4j.getExceptionInfo(e));
        }
        return false;
    }



    @Override
    public List<StatisticsUserMonth> selectAll(GetParameter parameter)
    {
        return userMonthMapper.selectAll();
    }
}
