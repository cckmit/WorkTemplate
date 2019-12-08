package com.fish.service;

import com.fish.dao.primary.mapper.StatisticsUserDayMapper;
import com.fish.dao.primary.mapper.StatisticsUserMonthMapper;
import com.fish.dao.primary.model.StatisticsUserDay;
import com.fish.dao.primary.model.StatisticsUserMonth;
import com.fish.protocols.GetParameter;
import com.fish.utils.XwhTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsUserDayService implements BaseService<StatisticsUserDay>
{
    @Autowired
    StatisticsUserDayMapper userDayMapper;
    @Autowired
    StatisticsUserMonthMapper userMonthMapper;

    @Override
    public void setDefaultSort(GetParameter parameter)
    {
        if (parameter.getOrder() != null)
            return;
        parameter.setSort("newDate");
        parameter.setOrder("desc");
    }

    @Override
    public Class<StatisticsUserDay> getClassInfo()
    {
        return StatisticsUserDay.class;
    }

    @Override
    public boolean removeIf(StatisticsUserDay statisticsUserDay, Map<String, String> searchData)
    {
        return existTimeFalse(statisticsUserDay.getNewDate(), searchData.get("time"));
    }

    @Override
    public List<StatisticsUserDay> selectAll(GetParameter parameter)
    {
        List<StatisticsUserDay> list = userDayMapper.selectAll();
        if (!list.isEmpty())
        {
            List<StatisticsUserMonth> months = userMonthMapper.selectAll();
            Map<Integer, Long> maus = new HashMap<>();
            months.forEach(month -> maus.put(month.getNewMonth(), month.getMau()));
            list.forEach(day ->
            {
                int month = XwhTool.getMonthValue(day.getNewDate());
                Long mau = maus.get(month);
                day.setMau(mau);
            });
        }
        return list;
    }
}
